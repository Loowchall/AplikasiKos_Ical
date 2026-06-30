// =========================================
// Hasil Translasi Class Diagram ke Kotlin
// Studi Kasus: Aplikasi Manajemen Kos
// =========================================

// -----------------------------------------
// Class: Student
// Atribut:
//   + Nama: String   -> public
//   + NIM: String    -> public
//   - Saldo: Double  -> private
// -----------------------------------------
class Student(
    var nama: String,
    var nim: String,
    private var saldo: Double = 0.0
) {
    // Method tambahan untuk mengakses saldo dari luar class
    // karena saldo bersifat private (sesuai diagram)
    fun getSaldo(): Double {
        return saldo
    }

    fun tambahSaldo(jumlah: Double) {
        saldo += jumlah
    }

    fun kurangiSaldo(jumlah: Double): Boolean {
        if (jumlah > saldo) {
            return false // saldo tidak cukup
        }
        saldo -= jumlah
        return true
    }
}

// -----------------------------------------
// Class: Professor (sesuai diagram, walau atributnya
// merepresentasikan data Kamar: nomor, tipe, harga)
// Atribut:
//   + nomor: Int     -> public
//   + tipe: String   -> public
//   + Harga: Double  -> public
// -----------------------------------------
class Professor(
    var nomor: Int,
    var tipe: String,
    var harga: Double
)
// Catatan: getNomor() dan getTipe() tidak perlu ditulis manual,
// karena Kotlin otomatis membuatkan getter dari property "nomor" dan "tipe"
// di atas. Cukup akses langsung lewat objek.nomor dan objek.tipe.

// -----------------------------------------
// Class: KostManager
// Atribut:
//   - Daftar kamar: List<Kamar>          -> private
//   - Daftar penghuni: Map<Int, Penghuni> -> private
// Method:
//   + tambahKamar(k: Kamar): void
//   + DaftarkanPenghuni(n,p): Boolean
//   + keluarkanPenghuni(n): Boolean
//   + ProsesPembayaran(n): void
//   + TampilkanStatus(): void
//
// Catatan: Karena tipe "Kamar" dan "Penghuni" tidak
// didefinisikan secara eksplisit di diagram, di sini
// kita pakai class Professor sebagai representasi Kamar
// dan Student sebagai representasi Penghuni, mengikuti
// relasi "Memiliki" pada diagram.
// -----------------------------------------
class KostManager {
    // Aggregation: KostManager "memiliki" daftar Kamar dan Penghuni
    private val daftarKamar: MutableList<Professor> = mutableListOf()
    private val daftarPenghuni: MutableMap<Int, Student> = mutableMapOf()

    // + tambahKamar(k: Kamar): void
    fun tambahKamar(k: Professor) {
        daftarKamar.add(k)
        println("Kamar nomor ${k.nomor} (${k.tipe}) berhasil ditambahkan.")
    }

    // + DaftarkanPenghuni(n, p): Boolean
    fun daftarkanPenghuni(nomorKamar: Int, penghuni: Student): Boolean {
        val kamar = daftarKamar.find { it.nomor == nomorKamar }
        if (kamar == null) {
            println("Kamar nomor $nomorKamar tidak ditemukan.")
            return false
        }
        if (daftarPenghuni.containsKey(nomorKamar)) {
            println("Kamar nomor $nomorKamar sudah terisi.")
            return false
        }
        daftarPenghuni[nomorKamar] = penghuni
        println("${penghuni.nama} berhasil didaftarkan ke kamar $nomorKamar.")
        return true
    }

    // + keluarkanPenghuni(n): Boolean
    fun keluarkanPenghuni(nomorKamar: Int): Boolean {
        if (!daftarPenghuni.containsKey(nomorKamar)) {
            println("Kamar nomor $nomorKamar tidak memiliki penghuni.")
            return false
        }
        daftarPenghuni.remove(nomorKamar)
        println("Penghuni kamar $nomorKamar berhasil dikeluarkan.")
        return true
    }

    // + ProsesPembayaran(n): void
    fun prosesPembayaran(nomorKamar: Int) {
        val kamar = daftarKamar.find { it.nomor == nomorKamar }
        val penghuni = daftarPenghuni[nomorKamar]

        if (kamar == null || penghuni == null) {
            println("Tidak dapat memproses pembayaran: kamar atau penghuni tidak ditemukan.")
            return
        }

        val berhasil = penghuni.kurangiSaldo(kamar.harga)
        if (berhasil) {
            println("Pembayaran kamar $nomorKamar oleh ${penghuni.nama} berhasil. Sisa saldo: ${penghuni.getSaldo()}")
        } else {
            println("Pembayaran gagal: saldo ${penghuni.nama} tidak mencukupi.")
        }
    }

    // + TampilkanStatus(): void
    fun tampilkanStatus() {
        println("===== STATUS KOS =====")
        for (kamar in daftarKamar) {
            val penghuni = daftarPenghuni[kamar.nomor]
            val statusPenghuni = penghuni?.nama ?: "Kosong"
            println("Kamar ${kamar.nomor} | Tipe: ${kamar.tipe} | Harga: ${kamar.harga} | Penghuni: $statusPenghuni")
        }
        println("=======================")
    }
}

// -----------------------------------------
// Uji Coba di main()
// -----------------------------------------
fun main() {
    val manager = KostManager()

    // Membuat data kamar (instansiasi Professor sebagai representasi Kamar)
    val kamar1 = Professor(nomor = 1, tipe = "Standar", harga = 800000.0)
    val kamar2 = Professor(nomor = 2, tipe = "VIP", harga = 1500000.0)

    manager.tambahKamar(kamar1)
    manager.tambahKamar(kamar2)

    // Membuat data penghuni (instansiasi Student)
    val budi = Student(nama = "Budi", nim = "12345", saldo = 1000000.0)

    // Daftarkan penghuni ke kamar
    manager.daftarkanPenghuni(1, budi)

    // Tampilkan status sebelum pembayaran
    manager.tampilkanStatus()

    // Proses pembayaran
    manager.prosesPembayaran(1)

    // Tampilkan status setelah pembayaran
    manager.tampilkanStatus()

    // Coba keluarkan penghuni
    manager.keluarkanPenghuni(1)
    manager.tampilkanStatus()
}