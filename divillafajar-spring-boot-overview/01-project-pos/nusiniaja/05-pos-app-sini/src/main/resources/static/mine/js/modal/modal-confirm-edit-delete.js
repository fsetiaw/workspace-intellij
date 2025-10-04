const itemList = document.getElementById("itemList");
const msgFailedUpdate = document.getElementById("toast-msg-update-failed").dataset.msg;
const msgSuccessUpdate = document.getElementById("toast-msg-update-success").dataset.msg;
let itemToDelete = null;
const deleteModal = new bootstrap.Modal(document.getElementById("danger"));
const confirmDeleteBtn = document.getElementById("confirmDeleteBtn");
const contextPath = document.querySelector("meta[name='contextPath']").content;
const pAid = document.querySelector("meta[name='pAid']").content;
const addItemBtn = document.getElementById("addItemBtn");
const newItemInput = document.getElementById("newItemInput");
// Event delegation
itemList.addEventListener("click", function(e) {
  const editBtn = e.target.closest(".edit-btn");
  const deleteBtn = e.target.closest(".delete-btn");

  // EDIT
  if (editBtn) {
    const li = editBtn.closest("li");
    const span = li.querySelector(".item-text");
    const buttons = li.querySelector(".buttons");
    const oldText = span.textContent.trim();

    // buat input
    const input = document.createElement("input");
    input.type = "text";
    input.className = "form-control form-control-sm flex-grow-1"; // biar isi sisa ruang
    input.value = oldText;

    // buat tombol enter
    const enterBtn = document.createElement("button");
    enterBtn.className = "btn btn-sm btn-success ms-2 flex-shrink-0";
    enterBtn.innerHTML = '<i class="bi bi-check"></i>';

    // bungkus input + tombol
    const wrapper = document.createElement("div");
    wrapper.className = "d-flex align-items-center w-100";
    wrapper.appendChild(input);
    wrapper.appendChild(enterBtn);

    // replace & hide tombol
    li.replaceChild(wrapper, span);
    buttons.style.display = "none";
    input.focus();

    let saved = false;

    // fungsi restore bila batal
    const restore = () => {
      const restoreSpan = document.createElement("span");
      restoreSpan.className = "item-text";
      restoreSpan.textContent = oldText;
      if (li.contains(wrapper)) li.replaceChild(restoreSpan, wrapper);
      buttons.style.display = "block";
    };

    // blur handler
    const onBlur = function() {
      if (!saved) restore();
    };
    input.addEventListener("blur", onBlur);

    // 👉 refactor ke satu fungsi
    const saveChange = () => {
      const newValue = input.value.trim() || oldText;

      // tandai saved biar blur ga revert
      saved = true;
      input.removeEventListener("blur", onBlur);

      // ganti wrapper -> span
      const newSpan = document.createElement("span");
      newSpan.className = "item-text";
      newSpan.textContent = newValue;
      if (li.contains(wrapper)) li.replaceChild(newSpan, wrapper);
      buttons.style.display = "block";

      // kalau sama persis (case sensitive) → stop
      if (newValue === oldText) {
        return;
      }

      const itemId = li.getAttribute("data-id") || "";
      const originalText = oldText;

      // indikator loading
      newSpan.textContent = `${newValue} (saving...)`;
      buttons.querySelectorAll("a").forEach(btn => btn.disabled = true);

      fetch(`${contextPath}api/v1/manager/product/category/${itemId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ id: itemId, name: newValue })
      })
      .then(res => {
        if (res.status === 204) return { id: itemId, name: newValue };
        if (!res.ok) throw new Error("Update failed");
        return res.json();
      })
      .then(data => {
        newSpan.textContent = data.name || newValue;
        buttons.style.display = "block";
        buttons.querySelectorAll("a").forEach(btn => btn.disabled = false);
        Toastify({
          text: oldText + " " + msgSuccessUpdate,
          duration: 2000,
          close: true,
          gravity: "bottom",
          position: "center",
          backgroundColor: "#4fbe87" // hijau success
        }).showToast();
      })
      .catch(err => {
        console.error(err);
        newSpan.textContent = originalText; // rollback
        buttons.style.display = "block";
        buttons.querySelectorAll("a").forEach(btn => btn.disabled = false);
        Toastify({
          text: oldText + " " + msgFailedUpdate,
          duration: 2000,
          close: true,
          gravity: "bottom",
          position: "center",
          backgroundColor: "#dc3545"
        }).showToast();
      });
    };

    // Enter keyboard
    input.addEventListener("keydown", function(evt) {
      if (evt.key === "Enter") {
        evt.preventDefault();
        saveChange();
      }
    });

    // Klik tombol ✔
    //enterBtn.addEventListener("click", function(evt) {
    enterBtn.addEventListener("mousedown", function(evt) {
      evt.preventDefault();
      saveChange();
    });
  }

  // DELETE
  if (deleteBtn) {
    itemToDelete = deleteBtn.closest("li");
    const itemName = itemToDelete.querySelector(".item-text").textContent;
    document.getElementById("deletedItemName").textContent = itemName;
    deleteModal.show();
  }
});

/* Konfirmasi delete
confirmDeleteBtn.addEventListener("click", function() {
  if (itemToDelete) {
    itemToDelete.remove();
    itemToDelete = null;
  }
  deleteModal.hide();
});
*/
addItemBtn.addEventListener("click", function() {
      const newValue = newItemInput.value.trim();
      if (!newValue) return;

      addItemBtn.disabled = true;

      fetch(`${contextPath}api/v1/manager/product/category`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            name: newValue,
            id: 0
        })
      })
      .then(res => {
        if (res.status === 201 || res.status === 200) return res.json();
        throw new Error("Add failed");
      })
      .then(data => {
        newItemInput.value = "";

        const li = document.createElement("li");
        li.className = "list-group-item d-flex justify-content-between align-items-center";
        li.setAttribute("data-id", data.id);

        const span = document.createElement("span");
        span.className = "item-text";
        span.textContent = data.name || newValue;

        const buttons = document.createElement("div");
        buttons.className = "buttons";
        buttons.innerHTML = `
          <a href="#" class="btn btn-sm btn-primary edit-btn"><i class="bi bi-pencil"></i></a>
          <a href="#" class="btn btn-sm btn-danger delete-btn"><i class="bi bi-trash"></i></a>
        `;

        li.appendChild(span);
        li.appendChild(buttons);
        itemList.appendChild(li);

        Toastify({
          text: `${data.name || newValue} ${msgSuccessUpdate}`,
          duration: 2000,
          close: true,
          gravity: "bottom",
          position: "center",
          backgroundColor: "#198754"
        }).showToast();
      })
      .catch(err => {
        console.error(err);
        Toastify({
          text: `${newValue} ${msgFailedUpdate}`,
          duration: 2000,
          close: true,
          gravity: "bottom",
          position: "center",
          backgroundColor: "#dc3545"
        }).showToast();
      })
      .finally(() => {
        addItemBtn.disabled = false;
      });
    });

   document.addEventListener("click", function(e) {
  const addSubBtn = e.target.closest(".add-sub-btn");
  if (addSubBtn) {
    e.preventDefault();

    const parentLi = addSubBtn.closest("li");
    const parentId = parentLi.getAttribute("data-id");

    // Cegah input ganda
    if (parentLi.querySelector(".sub-input-container")) return;

    // Sembunyikan tombol add-sub sementara
    addSubBtn.style.display = "none";

    // Buat elemen input subcategory
    const subInputContainer = document.createElement("div");
    subInputContainer.className = "input-group mt-2 sub-input-container";

    subInputContainer.innerHTML = `
  <div class="d-flex align-items-center ms-4" style="width:90%;">
    <input type="text" class="form-control form-control-sm sub-input" placeholder="Subkategori..." style="flex:1;"/>
    <button class="btn btn-success btn-sm save-sub-btn ms-2" type="button" style="padding:2px 4px; font-size:0.75rem;">
      <i class="bi bi-check"></i>
    </button>
    <button class="btn btn-secondary btn-sm cancel-sub-btn ms-1" type="button" style="padding:2px 4px; font-size:0.75rem;">
      <i class="bi bi-x"></i>
    </button>
  </div>
`;

    parentLi.insertAdjacentElement("afterend", subInputContainer);
    subInputContainer.querySelector(".sub-input").focus();
  }

  // Simpan subcategory
  if (e.target.closest(".save-sub-btn")) {
    const container = e.target.closest(".sub-input-container");
    const parentLi = container.previousElementSibling;
    const parentId = parentLi.getAttribute("data-id");
    const input = container.querySelector(".sub-input");
    const newValue = input.value.trim();
    if (!newValue) return;

    e.target.disabled = true;

    fetch(`${contextPath}api/v1/manager/product/category/sub`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        name: newValue,
        parentId: parentId,
        clientAddressPubId: pAid
      })
    })
    .then(res => {
      if (res.status === 201 || res.status === 200) return res.json();
      throw new Error("Add failed");
    })
    .then(data => {
      // Tambahkan subcategory di bawah parent
      const subLi = document.createElement("li");
      subLi.className = "list-group-item ps-5 d-flex justify-content-between align-items-center";
      subLi.setAttribute("data-id", data.id);

      subLi.innerHTML = `
        <span class="item-text">${data.name}</span>
        <div class="buttons">
          <a href="#" class="btn btn-sm btn-primary edit-btn"><i class="bi bi-pencil"></i></a>
          <a href="#" class="btn btn-sm btn-danger delete-btn"><i class="bi bi-trash"></i></a>
        </div>
      `;

      parentLi.insertAdjacentElement("afterend", subLi);
      container.remove();

      Toastify({
        text: `${data.name} berhasil ditambahkan`,
        duration: 2000,
        close: true,
        gravity: "bottom",
        position: "center",
        backgroundColor: "#198754"
      }).showToast();
    })
    .catch(err => {
      console.error(err);
      Toastify({
        text: `Gagal menambah subkategori`,
        duration: 2000,
        close: true,
        gravity: "bottom",
        position: "center",
        backgroundColor: "#dc3545"
      }).showToast();
    });
  }

  // Batalkan input
if (e.target.closest(".cancel-sub-btn")) {
  const container = e.target.closest(".sub-input-container");

  // Cari parent <li> terdekat sebelum input container
  let parentLi = container.previousElementSibling;
  while (parentLi && parentLi.tagName !== "LI") {
    parentLi = parentLi.previousElementSibling;
  }

  // Tampilkan lagi tombol add-sub jika ada
  if (parentLi) {
    const hiddenBtn = parentLi.querySelector(".add-sub-btn");
    if (hiddenBtn) hiddenBtn.style.display = "";
  }

  // Hapus input subcategory dari DOM
  container.remove();
}
});