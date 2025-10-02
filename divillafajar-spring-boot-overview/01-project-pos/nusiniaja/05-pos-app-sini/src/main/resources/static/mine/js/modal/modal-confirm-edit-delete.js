const itemList = document.getElementById("itemList");
const msgFailedUpdate = document.getElementById("toast-msg-update-failed").dataset.msg;
const msgSuccessUpdate = document.getElementById("toast-msg-update-success").dataset.msg;
let itemToDelete = null;
const deleteModal = new bootstrap.Modal(document.getElementById("danger"));
const confirmDeleteBtn = document.getElementById("confirmDeleteBtn");
const contextPath = document.querySelector("meta[name='contextPath']").content;
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

        // 👉 Tambahkan pengecekan ini
        if (newValue === oldText) {
          // tidak ada perubahan → cukup stop di sini
          return;
        }

        //4) buat form tersembunyi lalu submit (form-urlencoded)
        const itemId = li.getAttribute("data-id") || "";
        const originalText = oldText; // simpan teks asli sebelum "saving..."
        //const savingText = newSpan.textContent;

        // indikator loading
        newSpan.textContent = `${newValue} (saving...)`;

        // disable tombol sementara
        buttons.querySelectorAll("a").forEach(btn => btn.disabled = true);

        fetch(`${contextPath}api/v1/manager/product/category/${itemId}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ id: itemId, name: newValue })
        })
        .then(res => {
          if (res.status === 204) return { id: itemId, name: newValue }; // no content
          if (!res.ok) throw new Error("Update failed");
          return res.json();
        })
        .then(data => {
        newSpan.textContent = data.name || newValue; // update sukses
        buttons.style.display = "block";
        buttons.querySelectorAll("a").forEach(btn => btn.disabled = false);
        Toastify({
              text: oldText+' '+msgSuccessUpdate,
              duration: 2000,
              close: true,
              gravity: "bottom",
              position: "center",
              backgroundColor: "#4fbe87" // hijau success
          }).showToast();
          /* simulasi delay 1 detik
          setTimeout(() => {
              newSpan.textContent = data.name || newValue; // update sukses
              buttons.style.display = "block";
              buttons.querySelectorAll("a").forEach(btn => btn.disabled = false);
            }, 1000);
          */
        })
        .catch(err => {
          setTimeout(() => {
            console.error(err);
            newSpan.textContent = originalText; // rollback
            buttons.style.display = "block";
            buttons.querySelectorAll("a").forEach(btn => btn.disabled = false);
            //alert("Gagal update kategori, coba lagi.");
            Toastify({
                    text: oldText+' '+msgFailedUpdate,
                    duration: 2000,
                    close:true,
                    gravity:"bottom",
                    position: "center",
                    backgroundColor: "#dc3545",
                }).showToast();
            }, 1000);
        });


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