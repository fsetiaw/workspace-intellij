document.addEventListener("DOMContentLoaded", () => {
      // === phone input ===
    const form = document.querySelector("#myform");
    const input = document.querySelector("#phone");

    if (input) {
      const iti = window.intlTelInput(input, {
        initialCountry: "id",           // default Indonesia
        separateDialCode: true,         // tampilkan kode negara terpisah di UI
        formatOnDisplay: true,
        nationalMode: false,            // jangan tampilkan leading zero
        utilsScript:
          "https://cdnjs.cloudflare.com/ajax/libs/intl-tel-input/18.2.1/js/utils.js"
      });

      form.addEventListener("submit", function () {
        // simpan ke DB dalam format INTERNATIONAL (contoh: "+62 811 8118 999")
        input.value = iti.getNumber(intlTelInputUtils.numberFormat.INTERNATIONAL);
      });
    }

    const pwd = document.getElementById("pwd");

    function generateContent(value) {
        const checks = {
            length: value.length >= 8,
            lower: /[a-z]/.test(value),
            upper: /[A-Z]/.test(value),
            number: /\d/.test(value),
            special: /[\W_]/.test(value)
        };

        return `
        <ul class="small m-0" style="color:#004CC9;">
            <li style="color:${checks.length ? 'green':'red'}">Minimal 8 karakter</li>
            <li style="color:${checks.lower ? 'green':'red'}">Mengandung huruf kecil (a-z)</li>
            <li style="color:${checks.upper ? 'green':'red'}">Mengandung huruf besar (A-Z)</li>
            <li style="color:${checks.number ? 'green':'red'}">Mengandung angka (0-9)</li>
            <li style="color:${checks.special ? 'green':'red'}">Mengandung karakter spesial (!@#$...)</li>
        </ul>
        `;
    }

    // === init popover ===
    const popover = new bootstrap.Popover(pwd, {
        trigger: "focus hover",
        html: true,
        content: generateContent("")
    });

    // === update content saat user ketik ===
    pwd.addEventListener("input", () => {
        const instance = bootstrap.Popover.getInstance(pwd);
        if (instance && pwd.getAttribute("aria-describedby")) {
          const popoverId = pwd.getAttribute("aria-describedby");
          const popoverBody = document.querySelector(`#${popoverId} .popover-body`);
          if (popoverBody) {
            popoverBody.innerHTML = generateContent(pwd.value);
          }
        }
    });
});