let isShaking = false; // state global
const cards = document.querySelectorAll(".store-card");

document.addEventListener("DOMContentLoaded", function() {

    const clientType = document.getElementById("clientType");
    const otherWrapper = document.getElementById("otherFieldWrapper");
    const otherInput = document.getElementById("otherField");
    const togglePassword = document.getElementById("togglePassword");
    const passwordField = document.getElementById("pwd");
    // setelah page load → langsung sembunyikan semua tombol
    //const cards = document.querySelectorAll(".store-card");
    cards.forEach(card => {
        card.querySelectorAll(".delete-btn, .update-btn").forEach(btn => {
            btn.style.display = "none";
            btn.addEventListener("click", function(e) {
                e.stopPropagation();
                e.preventDefault();

                if (btn.classList.contains("delete-btn")) {
                  document.getElementById("confirmDeleteToast").style.display = "block";
                }
                //else if (btn.classList.contains("update-btn")) {
                //  document.getElementById("confirmUpdateToast").style.display = "block";
                //}
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
    //const dashboardMenu = document.querySelector('.sidebar-item[data-page="dashboard"]');
  const dashboardMenu = document.getElementById("dashboardMenu");
  const trigger = e.target.closest(".shake-trigger");
  if (!trigger) return; // kalau bukan click di trigger, abaikan

  e.preventDefault();
  const targetBtnClass = trigger.getAttribute("data-target-btn");
  //const cards = document.querySelectorAll(".store-card");

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

    <!-- UNTUK AKTIFKAN PARENT MENU DARI SUBMENU-->
    // cari parent .sidebar-item
      const parentMenu = trigger.closest(".sidebar-item");
      if (parentMenu) {
        // hapus active di semua menu
        document.querySelectorAll(".sidebar-item").forEach(li => li.classList.remove("active"));
        // aktifkan menu induk
        parentMenu.classList.add("active");
      }
    <!-- END UNTUK AKTIFKAN MAIN MENU NON DASHBOARD-->
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
    <!-- UNTUK NON-AKTIFKAN MAIN MENU NON DASHBOARD & DASHBOARD YG JADi ACTIVE-->
    document.querySelectorAll(".sidebar-item").forEach(li => li.classList.remove("active"));
    dashboardMenu.classList.add("active");
    <!-- END UNTUK NON-AKTIFKAN MAIN MENU NON DASHBOARD & DASHBOARD YG JADi ACTIVE-->
    document.querySelectorAll(".shake-trigger.shake").forEach(t => t.classList.remove("shake"));


    /* cari elemen sidebar berdasarkan attribute custom (lebih aman)
    const dashboardMenu = document.querySelector('.sidebar-item[data-page="dashboard"]');
    */
    // hapus active di semua menu
        //
        //dashboardMenu.classList.add('active');

    isShaking = false;
  }
});

<!-- buat handle delete-->
const toast = document.getElementById("confirmDeleteToast");
const confirmMessage = document.getElementById("confirmMessage");
const cancelDelete = document.getElementById("cancelDelete");
const confirmDelete = document.getElementById("confirmDelete");
const msgConfirmDeleteQuestion = document.getElementById("msgConfirmDeleteQuestion").textContent; //message properties

let currentCard = null; // simpan card yang sedang dipilih

document.querySelectorAll(".delete-btn").forEach(btn => {
  btn.addEventListener("click", function () {
    const card = this.closest(".store-card");
    const storeName = this.getAttribute("data-store-name");

    currentCard = card;

    confirmMessage.textContent = `${msgConfirmDeleteQuestion} ${storeName}?`;
    toast.classList.add("show");
  });
});

// batal
cancelDelete.addEventListener("click", () => {
    console.log("Cancel clicked, classList:", toast.className);
    //console.log('confirmDeleteToast count:', document.querySelectorAll('#confirmDeleteToast').length);
    //console.log('.toast-confirm count:', document.querySelectorAll('.toast-confirm').length);
    toast.classList.remove("show");
    toast.style.display = "none"; // force hide
    // stop goyang
        cards.forEach(card => {
            card.classList.remove("shake");
            // enable lagi link
            card.querySelectorAll("a.text-decoration-none").forEach(link => link.classList.remove("disabled-link"));
        });
        document.querySelectorAll(".shake-trigger.shake").forEach(t => t.classList.remove("shake"));
        isShaking = false;
        // sembunyikan semua tombol dulu
          cards.forEach(card => {
            card.querySelectorAll(".delete-btn, .update-btn").forEach(btn => btn.style.display = "none");
          });
    currentCard = null;
    // reset semua active sidebar & submenu
        document.querySelectorAll(".sidebar-item.active, .submenu-item.active")
            .forEach(el => el.classList.remove("active"));

        // khusus matikan juga sidebarMenu1 kalau ada
        const sidebarMenu1 = document.getElementById("dashboardMenu");
        if (sidebarMenu1) {
            sidebarMenu1.classList.add("active");
        }

});

// konfirmasi
confirmDelete.addEventListener("click", () => {
   /*
  if (currentCard) {
    currentCard.remove(); // hapus card dari DOM
    toast.style.display = "none";
    currentCard = null;
  }
  */
  if (currentCard) {
      const storeName = currentCard.querySelector(".delete-btn").getAttribute("data-store-name");
      const targetKeyValue = currentCard.querySelector(".delete-btn").getAttribute("data-store-pid"); //Data dari <button terkait di main.htmlnya

      const form = document.getElementById("deleteForm"); //deleteClientAddress dari nama Form @logout-and-other-hidden-form
      const formField = document.getElementById("pubId"); //pubId dari field Form @logout-and-other-hidden-form

      formField.value = targetKeyValue;
      form.submit();

    }
  toast.classList.remove("show");
});


/*<!-- buat handle update-->
const toastUpdate = document.getElementById("confirmUpdateToast");
const confirmUpdateMessage = document.getElementById("confirmUpdateMessage");
const cancelUpdate = document.getElementById("cancelUpdate");
const confirmUpdate = document.getElementById("confirmUpdate");
const msgConfirmUpdateQuestion = document.getElementById("msgConfirmUpdateQuestion").textContent;



document.querySelectorAll(".update-btn").forEach(btn => {
  btn.addEventListener("click", function () {
    const card = this.closest(".store-card");
    const storeName = this.getAttribute("data-store-name");

    currentCard = card;

    //confirmUpdateMessage.textContent = `Anda yakin mau update ${storeName}?`;
    confirmUpdateMessage.textContent = `${msgConfirmUpdateQuestion} ${storeName}?`;
    toastUpdate.classList.add("show");

  });
});

// batal
cancelUpdate.addEventListener("click", () => {
    toastUpdate.classList.remove("show");
    toastUpdate.style.display = "none"; // force hide

    // stop goyang
    cards.forEach(card => {
        card.classList.remove("shake");
        // enable lagi link
        card.querySelectorAll("a.text-decoration-none").forEach(link => link.classList.remove("disabled-link"));
    });
    document.querySelectorAll(".shake-trigger.shake").forEach(t => t.classList.remove("shake"));
    isShaking = false;
    // sembunyikan semua tombol dulu
      cards.forEach(card => {
        card.querySelectorAll(".delete-btn, .update-btn").forEach(btn => btn.style.display = "none");
      });
    currentCard = null;
   // reset semua active sidebar & submenu
    document.querySelectorAll(".sidebar-item.active, .submenu-item.active")
        .forEach(el => el.classList.remove("active"));

    // khusus matikan juga sidebarMenu1 kalau ada
    const sidebarMenu1 = document.getElementById("dashboardMenu");
    if (sidebarMenu1) {
        sidebarMenu1.classList.add("active");
    }
});

// konfirmasi
confirmUpdate.addEventListener("click", () => {
  if (currentCard) {
    const storeName = currentCard.querySelector(".update-btn").getAttribute("data-store-name");
    const targetKeyValue = currentCard.querySelector(".update-btn").getAttribute("data-store-pid");
    // set action ke path sesuai storeName
    const form = document.getElementById("updateForm");
    const formField = document.getElementById("updPubId");

    formField.value = targetKeyValue;
    form.submit();

  }
  toastUpdate.classList.remove("show");
});
*/

// === Handle langsung submit update tanpa konfirmasi ===
document.querySelectorAll(".update-btn").forEach(btn => {
  btn.addEventListener("click", function (e) {
    e.preventDefault(); // cegah link/button default behavior
    e.stopPropagation(); // biar nggak trigger event card click

    const targetKeyValue = this.getAttribute("data-store-pid"); // ambil ID dari tombol
    const form = document.getElementById("updateForm");
    const formField = document.getElementById("updPubId");

    formField.value = targetKeyValue;
    form.submit(); // langsung submit
  });
});


// === tambahan buat sync QuickMenu → Sidebar ===
document.querySelectorAll('.shake-trigger[data-submenu]').forEach(trigger => {
  trigger.addEventListener('click', function(e) {
    e.preventDefault();
    const submenuKey = this.getAttribute('data-submenu');

    // reset semua active
    document.querySelectorAll('.sidebar-item, .submenu-item').forEach(li => li.classList.remove('active'));
    if (submenuKey === 'menu1SubMenu') {
        sidebarMenu1.classList.add("active");
    }


  });
});