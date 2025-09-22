let isShaking = false; // state global

document.addEventListener("DOMContentLoaded", function() {

    const clientType = document.getElementById("clientType");
    const otherWrapper = document.getElementById("otherFieldWrapper");
    const otherInput = document.getElementById("otherField");
    const togglePassword = document.getElementById("togglePassword");
    const passwordField = document.getElementById("pwd");
    // setelah page load → langsung sembunyikan semua tombol
    const cards = document.querySelectorAll(".store-card");
    cards.forEach(card => {
        card.querySelectorAll(".delete-btn, .update-btn").forEach(btn => {
            btn.style.display = "none";
            btn.addEventListener("click", function(e) {
                e.stopPropagation();
                e.preventDefault();

                if (btn.classList.contains("delete-btn")) {
                  document.getElementById("confirmDeleteToast").style.display = "block";
                } else if (btn.classList.contains("update-btn")) {
                  document.getElementById("confirmUpdateToast").style.display = "block";
                }
              });
        });
    });

    togglePassword.addEventListener("click", function() {
        const type = passwordField.getAttribute("type") === "password" ? "text" : "password";
        passwordField.setAttribute("type", type);

        // toggle icon
        this.querySelector("i").classList.toggle("bi-eye");
        this.querySelector("i").classList.toggle("bi-eye-slash");
    });



    clientType.addEventListener("change", function () {
        if (this.value === "oth") {
            otherWrapper.style.display = "block";
            otherInput.setAttribute("required", "true");
        } else {
            otherWrapper.style.display = "none";
            otherInput.removeAttribute("required");
            otherInput.value = ""; // reset biar gak ikut submit
        }
    });
});


document.addEventListener("click", function(e) {
  const trigger = e.target.closest(".shake-trigger");
  if (!trigger) return; // kalau bukan click di trigger, abaikan

  e.preventDefault();
  const targetBtnClass = trigger.getAttribute("data-target-btn");
  const cards = document.querySelectorAll(".store-card");

  // sembunyikan semua tombol dulu
  cards.forEach(card => {
    card.querySelectorAll(".delete-btn, .update-btn").forEach(btn => btn.style.display = "none");
  });

  if (!isShaking) {
    // mulai goyang
    cards.forEach(card => {
      card.classList.add("shake");
      // disable semua link di dalam card
      card.querySelectorAll("a.text-decoration-none").forEach(link => link.classList.add("disabled-link"));

      // hanya tampilkan tombol target
      card.querySelectorAll(`.${targetBtnClass}`).forEach(btn => btn.style.display = "inline-block");
    });

    // kasih efek highlight di trigger yang diklik
    //trigger.classList.add("shake");
    document.querySelectorAll(`.shake-trigger[data-target-btn="${targetBtnClass}"]`)
        .forEach(t => t.classList.add("shake"));

    isShaking = true;
  } else {
    // stop goyang
    cards.forEach(card => {
        card.classList.remove("shake");
        // enable lagi link
        card.querySelectorAll("a.text-decoration-none").forEach(link => link.classList.remove("disabled-link"));
    });
    document.querySelectorAll(".shake-trigger.shake").forEach(t => t.classList.remove("shake"));
    isShaking = false;
  }
});

<!-- buat handle delete-->
const toast = document.getElementById("confirmDeleteToast");
const confirmMessage = document.getElementById("confirmMessage");
const cancelDelete = document.getElementById("cancelDelete");
const confirmDelete = document.getElementById("confirmDelete");

let currentCard = null; // simpan card yang sedang dipilih

document.querySelectorAll(".delete-btn").forEach(btn => {
  btn.addEventListener("click", function () {
    const card = this.closest(".store-card");
    const storeName = this.getAttribute("data-store-name");

    currentCard = card;

    confirmMessage.textContent = `Anda yakin mau hapus ${storeName}?`;
    toast.classList.add("show");
  });
});

// batal
cancelDelete.addEventListener("click", () => {
  toast.classList.remove("show");
  currentCard = null;
});

// konfirmasi
confirmDelete.addEventListener("click", () => {
  if (currentCard) {
    currentCard.remove(); // hapus card dari DOM
    currentCard = null;
  }
  toast.classList.remove("show");
});

<!-- buat handle update-->
const toastUpdate = document.getElementById("confirmUpdateToast");
const confirmUpdateMessage = document.getElementById("confirmUpdateMessage");
const cancelUpdate = document.getElementById("cancelUpdate");
const confirmUpdate = document.getElementById("confirmUpdate");



document.querySelectorAll(".update-btn").forEach(btn => {
  btn.addEventListener("click", function () {
    const card = this.closest(".store-card");
    const storeName = this.getAttribute("data-store-name");

    currentCard = card;

    confirmUpdateMessage.textContent = `Anda yakin mau update ${storeName}?`;
    toastUpdate.classList.add("show");
  });
});

// batal
cancelUpdate.addEventListener("click", () => {
  toastUpdate.classList.remove("show");
  currentCard = null;
});

// konfirmasi
confirmUpdate.addEventListener("click", () => {
  if (currentCard) {
    const storeName = currentCard.querySelector(".update-btn").getAttribute("data-store-name");
    const storePid = currentCard.querySelector(".update-btn").getAttribute("data-store-pid");
    // set action ke path sesuai storeName
    const form = document.getElementById("updateForm");
    //form.setAttribute("action", `/store/update/${storeName}`);

    //form.setAttribute("action", `/v1/superuser/clients/upd?pid=${pid}`);


    const pidField = document.getElementById("pidField");

    pidField.value = storePid;
    form.submit();

  }
  toastUpdate.classList.remove("show");
});

