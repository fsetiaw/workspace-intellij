const itemList = document.getElementById("itemList");
let itemToDelete = null;
const deleteModal = new bootstrap.Modal(document.getElementById("danger"));
const confirmDeleteBtn = document.getElementById("confirmDeleteBtn");

// Event delegation
itemList.addEventListener("click", function(e) {
  // jangan preventDefault di sini (biar Enter/submit bisa berjalan normal di input handler)
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
    input.className = "form-control form-control-sm";
    input.value = oldText;

    // replace & hide tombol
    li.replaceChild(input, span);
    buttons.style.display = "none";
    input.focus();

    let saved = false;

    // named blur handler supaya bisa di-remove
    const onBlur = function() {
      if (!saved) {
        // restore teks lama
        const restoreSpan = document.createElement("span");
        restoreSpan.className = "item-text";
        restoreSpan.textContent = oldText;
        // jika input masih ada, ganti
        if (li.contains(input)) li.replaceChild(restoreSpan, input);
        buttons.style.display = "block";
      }
    };

    input.addEventListener("blur", onBlur);

    input.addEventListener("keydown", function(evt) {
      if (evt.key === "Enter") {
        evt.preventDefault();

        const newValue = input.value.trim() || oldText;

        // 1) tandai saved dulu (penting supaya blur tidak revert)
        saved = true;

        // 2) lepas blur handler (aman)
        input.removeEventListener("blur", onBlur);

        // 3) ganti input -> span (UI immediate)
        const newSpan = document.createElement("span");
        newSpan.className = "item-text";
        newSpan.textContent = newValue;
        if (li.contains(input)) li.replaceChild(newSpan, input);
        buttons.style.display = "block";

        /* 4) buat form tersembunyi lalu submit (form-urlencoded)
        const itemId = li.getAttribute("data-id") || "";

        const form = document.createElement("form");
        form.method = "post";
        form.action = "/manage/product/cat";

        // hidden id
        const hidId = document.createElement("input");
        hidId.type = "hidden";
        hidId.name = "id";
        hidId.value = itemId;
        form.appendChild(hidId);

        // hidden name
        const hidName = document.createElement("input");
        hidName.type = "hidden";
        hidName.name = "name";
        hidName.value = newValue;
        form.appendChild(hidName);

        // jika Spring Security CSRF token tersedia di <meta>, tambahkan juga
        const csrfMeta = document.querySelector('meta[name="_csrf"]');
        if (csrfMeta) {
          const csrfInput = document.createElement("input");
          csrfInput.type = "hidden";
          csrfInput.name = "_csrf";
          csrfInput.value = csrfMeta.getAttribute("content");
          form.appendChild(csrfInput);
        }

        document.body.appendChild(form);
        form.submit();
        // (opsional) form.remove(); // halaman kemungkinan redirect jadi tidak perlu cleanup
        */
      }
    });
  }

  // Tombol delete
  if (e.target.closest(".delete-btn")) {
    itemToDelete = e.target.closest("li");
    const itemName = itemToDelete.querySelector(".item-text").textContent;

    // Set nama item ke modal
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