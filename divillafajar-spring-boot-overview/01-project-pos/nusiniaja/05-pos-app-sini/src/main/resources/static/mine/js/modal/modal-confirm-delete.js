const itemList = document.getElementById("itemList");
let itemToDelete = null;
const deleteModal = new bootstrap.Modal(document.getElementById("danger"));
const confirmDeleteBtn = document.getElementById("confirmDeleteBtn");

// Event delegation
itemList.addEventListener("click", function(e) {
  e.preventDefault();

  // Tombol edit
  if (e.target.closest(".edit-btn")) {
    const li = e.target.closest("li");
    const span = li.querySelector(".item-text");
    const buttons = li.querySelector(".buttons");

    // Simpan teks lama
    const oldText = span.textContent;

    // Ganti span ke input
    const input = document.createElement("input");
    input.type = "text";
    input.className = "form-control form-control-sm";
    input.value = oldText;

    li.replaceChild(input, span);
    buttons.style.display = "none"; // sembunyikan tombol
    input.focus();

    let saved = false; // flag

    // Saat enter → simpan perubahan
    input.addEventListener("keydown", function(evt) {
      if (evt.key === "Enter") {
        evt.preventDefault(); // jangan submit default

        const newValue = input.value.trim() || oldText;

        // Ubah ke span lagi
        const newSpan = document.createElement("span");
        newSpan.className = "item-text";
        newSpan.textContent = newValue;
        li.replaceChild(newSpan, input);
        buttons.style.display = "block";
        saved = true; // tandai sudah disimpan
      }
    });

    // Saat blur → batal edit, restore teks lama
    input.addEventListener("blur", function() {
      if (!saved) { // hanya revert kalau belum disimpan
        const newSpan = document.createElement("span");
        newSpan.className = "item-text";
        newSpan.textContent = oldText;
        li.replaceChild(newSpan, input);
        buttons.style.display = "block";
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