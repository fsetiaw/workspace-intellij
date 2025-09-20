document.addEventListener("DOMContentLoaded", function() {
  // Animasi masuk saat halaman load
  document.body.classList.add("page-transition-enter");

  // Intercept semua link normal
  document.querySelectorAll("a[href]").forEach(link => {
    // skip link dengan target="_blank" atau anchor (#)
    if (link.target === "_blank" || link.getAttribute("href").startsWith("#")) return;

    link.addEventListener("click", function(e) {
      e.preventDefault(); // tahan dulu default navigation
      const url = this.href;

      // Tambahkan animasi keluar
      document.body.classList.remove("page-transition-enter");
      document.body.classList.add("page-transition-exit");

      // Setelah animasi selesai (0.4s), baru redirect
      setTimeout(() => {
        window.location.href = url;
      }, 400); // harus sama dengan durasi animasi CSS
    });
  });
});