let users_global = [];
let function_global=[];
let billBooks =[1,2,3,4];
function loadHTML(filename) {
	onInit(filename);
	fetch(filename)
		.then(response => {
			if (!response.ok) throw new Error("Page not found");
			return response.text();
		})
		.then(html => {

			document.getElementById("content").innerHTML = html;
		})
		.catch(err => {
			document.getElementById("content").innerHTML = `<p>Error: ${err.message}</p>`;
		});
}

/*-------------------------------------------------------------------------------------------------------------------------------------------
 
						on In function

----------------------------------------------------------------------------------------------------------------------------------------------*/

function onInit(filename) {

	if (filename == 'view-user.html') {

		loadFunctionTypeList();

	}



}














/*-------------------------------------------------------------------------------------------------------------------------------------------
 
						on In function

----------------------------------------------------------------------------------------------------------------------------------------------*/
async function loadFunctionTypeList() {
    showLoading();
	try {
		const res = await fetch(`/api/user/functionTypes`);
		const result = await res.json();
		if (result.status === "Success" && result.data) {

			functionTypes = result.data;
			function_global= functionTypes;
			const list = document.getElementById("functionTypeList");
			console.log(functionTypes)

			functionTypes.forEach((fn, index) => {
				const li = document.createElement("li");
				li.innerHTML = `
          <label>
            <input type="checkbox" name="functionType" value="${index}" />
            ${fn.functionName}
          </label>`;
				list.appendChild(li);
			});
			 // Allow only one checkbox selection
                        list.addEventListener("change", function (e) {
                            if (e.target && e.target.name === "functionType") {
                                const checkboxes = list.querySelectorAll('input[type="checkbox"]');
                                checkboxes.forEach(cb => {
                                    if (cb !== e.target) cb.checked = false;
                                });
                            }
                        });

                        hideLoading();
		}
	} catch (error) {
		console.error("Error:", error);
		noResults.style.display = "block";
	}

}







/*-------------------------------------------------------------------------------------------------------------------------------------------
 
						search by phone number

----------------------------------------------------------------------------------------------------------------------------------------------*/

async function searchUser() {
	document.querySelector("details").open = false;
	const phone = document.getElementById("phone").value.trim();
	const overlay = document.getElementById("loadingOverlay");
	const noResults = document.getElementById("noResults");
	const userCards = document.getElementById("userCards");
	const errorMsg = document.getElementById("dropdownError"); // error element (if you have one)

	// Get selected function types
	const selectedIndexes = Array.from(document.querySelectorAll('input[name="functionType"]:checked'))
		.map(cb => parseInt(cb.value));

	if (selectedIndexes.length === 0) {
		// if (errorMsg) errorMsg.style.display = "block";
		// showToast('success', '✅ Certificate Created Successfully!');
		showToast('error', '❌ please select function type!');
		// showToast('info', 'ℹ️ Info: Please try again.')
		return;
	}
	if (errorMsg) errorMsg.style.display = "none";

	// Build functiontype array from global `functionTypes`
	const selectedFunctionTypes = selectedIndexes.map(i => ({
		SNO: functionTypes[i].SNO,
		uuid: functionTypes[i].UUID,
		netAmount: functionTypes[i].netAmount,
		functionName: functionTypes[i].functionName
	}));

	const requestBody = {
		phone: phone,
		functiontype: selectedFunctionTypes
	};

	overlay.style.display = "flex";
	noResults.style.display = "none";
	userCards.innerHTML = "";
 showLoading();
	try {
		const res = await fetch(`/api/user/get`, {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(requestBody)
		});

		const response = await res.json();
		overlay.style.display = "none";
        hideLoading();
		if (response.status === "Success" && response.data) {
			let users = Array.isArray(response.data) ? response.data : [response.data];

			if (users.length > 0) {
				renderCards(users);
				noResults.style.display = "none";
			} else {
				noResults.style.display = "block";
			}
		} else {
			noResults.style.display = "block";
		}
	} catch (error) {

        showErrorPopup();
	}
}





function submitUserForm(e) {
	e.preventDefault();

	const form = document.getElementById("userForm");


	const data = {
		name: form.name.value,
		phone: form.phone.value,
		fatherName: form.fatherName.value,
		address: form.address.value,
		city: form.city.value,
		district: form.district.value,
		pincode: form.pincode.value
	};

	fetch("/api/user/add", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify(data)
	})
		.then(res => res.text())
		.then(msg => {
			form.reset();
			showToast('success', '✅ User Created Successfully!');;
		}

		)
		.catch(() => messageBox.innerText = "Failed to add user");
}

function renderCards(users) {
	const container = document.getElementById("userCards");
	container.innerHTML = "";

	users_global=users;

	users.forEach((user, index) => {
		container.innerHTML += `
      <div class="user-card" id="card-${index}">
        <div class="user-card-top">
        <div class="user-card-side">
          <div class="user-info">
            <div class="user-name">${user.name}</div>
            <div class="user-detail">Phone: ${user.phone}</div>
            <div class="user-detail">City: ${user.city}</div>
          </div>
        </div>
        
        <div>
        <div class="amount-card-container">
    <div class="amount-card green">
      <h3>Total</h3>
      <p>${user.headderCalculation.totalAmount || 0.00}</p>
    </div>
    <div class="amount-card blue">
      <h3>Paid</h3>
      <p>${user.headderCalculation.paidAmount || 0.00}</p>
    </div>
    <div class="amount-card orange">
      <h3>Pending</h3>
      <p>${user.headderCalculation.balanceAmount || 0.00}</p>
    </div>
  </div>
  </div>
        
        <div class="user-actions">
          <button class="btn-view" onclick="toggleDetails(${index})">View</button>
          <button class="btn-edit" onclick="addPaymentRow(${index})">Add Bill</button>
          <button class="btn-delete">Delete</button>
        </div>
        </div>
        <div class="expand-details" id="details-${index}">
          <p><strong>Father Name:</strong> ${user.fatherName || "N/A"}</p>
          <p><strong>Address:</strong> ${user.address || "N/A"}</p>
          <p><strong>District:</strong> ${user.district || "N/A"}</p>
          <p><strong>Pincode:</strong> ${user.pincode || "N/A"}</p>
          
          <table class="payment-table" id="payment-table-${index}">
    <thead>
      <tr>
        <th>S.No</th>
        <th>Particular</th>
        <th>Date</th>
        <th>Amount</th>
        <th>Collected By</th>
      </tr>
    </thead>
    <tbody>
      ${user.functionPaymentList?.map((pd, i) => `
          <tr>
            <td>${i + 1}</td>
            <td>${pd.particulers}</td>
            <td>${pd.date}</td>
            <td>${pd.amount}</td>
            <td>${pd.colletedBy}</td>
          </tr>
        `).join("") || ` <tr id="noDataRow"><td colspan="5">No payment data</td></tr>`
			}
    </tbody>
  </table>
          
          
        </div>
      </div>
    `;
	});
}

function toggleDetails(index) {
	const section = document.getElementById(`details-${index}`);
	const btn = document.querySelector(`#card-${index} .btn-view`);
	const isVisible = section.style.display === "block";

	section.style.display = isVisible ? "none" : "block";
	btn.textContent = isVisible ? "View" : "Hide";
}


function addPaymentRow(index) {
	const tableBody = document.querySelector(`#payment-table-${index} tbody`);
	const noDataRow = tableBody.querySelector("#noDataRow");

	// Remove "No payment data" row if present
	if (noDataRow) {
		noDataRow.remove();
	}

	// Prevent duplicate input row
	if (document.querySelector(`#save-row-${index}`)) return;

	// Calculate serial number based on current rows (excluding input row)
	const rowCount = tableBody.querySelectorAll("tr:not(#save-row-" + index + ")").length + 1;

	// Create the editable row
	const newRow = document.createElement("tr");
	newRow.id = `save-row-${index}`;
	newRow.innerHTML = `
    <td>${rowCount}</td>
    <td><input type="text" placeholder="Particular" /></td>
    <td><input type="date" /></td>
    <td><input type="number" placeholder="Amount" /></td>
    <td><input type="text" placeholder="Collected By" /></td>
  `;
	tableBody.appendChild(newRow);

	// Prevent multiple Save buttons
	if (document.querySelector(`#save-btn-${index}`)) return;

	// Create Save Button
	const saveBtn = document.createElement("button");
	saveBtn.textContent = "Save";
	saveBtn.id = `save-btn-${index}`;
	saveBtn.className = "btn-save";
	saveBtn.style.margin = "10px 20px";
	saveBtn.onclick = () => savePaymentRow(index);
console.log(index);
	document.getElementById(`details-${index}`).appendChild(saveBtn);
}

function openPaymentModal(paymentData) {
// Fill header
    document.getElementById("paymentUserInfo").innerHTML =
        `<strong>${paymentData.name}</strong>
         (Phone: ${paymentData.phone}, City: ${paymentData.city})`;

    // Prefill hidden / existing fields
    document.getElementById("modalUserId").value = paymentData.userId;
    document.getElementById("modalUserPhone").value = paymentData.phone;
    document.getElementById("modalUserUUID").value = paymentData.UUID;
    document.getElementById("modalParticular").value = paymentData.particular || "";
    document.getElementById("modalDate").value = paymentData.date || "";
    document.getElementById("modalAmount").value = paymentData.amount || "";
    document.getElementById("modalCollectedBy").value = paymentData.collectedBy || "";

    // --- NEW FIELDS ---

    // 1. Function List Dropdown
    const fnSelect = document.getElementById("modalFunctionList");
    console.log(function_global);
    fnSelect.innerHTML = ""; // clear old options
    function_global.forEach(fn => {
        const opt = document.createElement("option");
        opt.value = fn.SNO; // function ID
        opt.textContent = fn.functionName; // function Name
        opt.dataset.fn = JSON.stringify(fn);
        fnSelect.appendChild(opt);
    });

    // Preselect if paymentData already has fnId
    if (paymentData.fnId) {
        fnSelect.value = paymentData.fnId;
    }

    // 2. Bill Number
    document.getElementById("modalBillNo").value = paymentData.billNo || "";

    // 3. Bill Book Person Name
    document.getElementById("modalBillPerson").value = paymentData.billPerson || "";

    // 4. Bill Book Number Dropdown
    const bookSelect = document.getElementById("modalBillBookNo");
    bookSelect.innerHTML = "";
    billBooks.forEach(book => {
        const opt = document.createElement("option");
        opt.value = book;
        opt.textContent = `Book ${book}`;
        bookSelect.appendChild(opt);
    });

    if (paymentData.billBookNo) {
        bookSelect.value = paymentData.billBookNo;
    }

    // Show modal
    document.getElementById("paymentModal").style.display = "flex";
}

function closePaymentModal() {
    document.getElementById("paymentModal").style.display = "none";
}

function savePaymentRow(index) {
    const row = document.querySelector(`#save-row-${index}`);
    if (!row) return;
console.log(users_global);
    const inputs = row.querySelectorAll("input");

//    let fn_type={
//         SNO : users_global[index];
//    	 UUID;
//    	 functionName;
//    	 showFlag;
//    	 netAmount;
//
//    };

    const paymentData = {
        userId: users_global[index].SNO,
        name: users_global[index].name,
        phone: users_global[index].phone,
        UUID: users_global[index].UUID,
        city: users_global[index].city,
        particular: inputs[0].value,
        date: inputs[1].value,
        amount: inputs[2].value,
        collectedBy: inputs[3].value
    };

    openPaymentModal(paymentData);
}

async function submitPayment() {
        const functionSelect = document.getElementById("modalFunctionList");
        const selectedOption = functionSelect.options[functionSelect.selectedIndex];
        console.log(selectedOption);
        const fnObj = selectedOption ? JSON.parse(selectedOption.dataset.fn) : null;

        const selectedfn = {
        sno : fnObj.SNO,
        uuid: fnObj.UUID,
        functionName: fnObj.functionName,
        netAmount : fnObj.netAmount,
        showFlag : fnObj.showFlag

        }

        const paymentData = {
            userSno: document.getElementById("modalUserId").value,
            particular: document.getElementById("modalParticular").value,
            date: document.getElementById("modalDate").value,
            amount: document.getElementById("modalAmount").value,
            collectedBy: document.getElementById("modalCollectedBy").value,
            phone : document.getElementById("modalUserPhone").value,
            userId : document.getElementById("modalUserUUID").value,

            functionType: selectedfn,  // ✅ send full function object here

            billNo: document.getElementById("modalBillNo").value,
            billPerson: document.getElementById("modalBillPerson").value,
            billBookNo: document.getElementById("modalBillBookNo").value,
        };

        try {
            const res = await fetch("/api/user/savePayment", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(paymentData)
            });

            if (res.ok) {
                alert("✅ Payment saved successfully!");
                closePaymentModal();
                location.reload();
            } else {
                alert("❌ Failed to save payment.");
            }
        } catch (err) {
            console.error(err);
            alert("⚠️ Error while saving payment.");
        }


}


function showToast(type, message) {
	const toast = document.createElement('div');
	toast.className = `toast ${type}`;
	toast.textContent = message;

	document.getElementById('toastContainer').appendChild(toast);

	setTimeout(() => {
		toast.remove();
	}, 4000); // Auto-dismiss after 3 seconds
}

function showErrorPopup() {
  const popup = document.getElementById("errorPopup");
  if (popup) popup.style.display = "flex";
}

function closePopup() {
  const popup = document.getElementById("errorPopup");
  if (popup) popup.style.display = "none";
}


function showLoading() {
  const loader = document.getElementById('globalLoader');
  if (loader) loader.style.display = 'flex';
}

function hideLoading() {
  const loader = document.getElementById('globalLoader');
  if (loader) loader.style.display = 'none';
}





