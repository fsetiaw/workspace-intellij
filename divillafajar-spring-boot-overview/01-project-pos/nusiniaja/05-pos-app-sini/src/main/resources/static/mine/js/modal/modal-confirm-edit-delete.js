const itemList = document.getElementById("itemList");
const msgFailedUpdate = document.getElementById("toast-msg-update-failed").dataset.msg;
const msgSuccessUpdate = document.getElementById("toast-msg-update-success").dataset.msg;
let itemToDelete = null;
const deleteModal = new bootstrap.Modal(document.getElementById("danger"));
const confirmDeleteBtn = document.getElementById("confirmDeleteBtn");
const contextPath = document.querySelector("meta[name='contextPath']").content;

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

// Konfirmasi delete
confirmDeleteBtn.addEventListener("click", function() {
  if (itemToDelete) {
    itemToDelete.remove();
    itemToDelete = null;
  }
  deleteModal.hide();
});
