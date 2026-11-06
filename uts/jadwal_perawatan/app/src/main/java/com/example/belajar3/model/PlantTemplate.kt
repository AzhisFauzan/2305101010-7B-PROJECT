package com.example.belajar3.model

data class TemplateTask(
    val targetHst: Int,
    val careType: CareType,
    val title: String,
    val description: String
)

data class PlantTemplate(
    val id: String,
    val name: String,
    val defaultVariety: String,
    val estimatedHarvestHst: Int,
    val description: String,
    val tasks: List<TemplateTask>
) {
    companion object {
        fun getAllTemplates(): List<PlantTemplate> = listOf(
            PlantTemplate(
                id = "cabai",
                name = "Cabai (Rawit / Merah)",
                defaultVariety = "Ori 212 / Roger / TM99",
                estimatedHarvestHst = 85,
                description = "Template perawatan lengkap cabai dari olah lahan, masa vegetatif cor NPK, wiwil tunas air hingga panen.",
                tasks = listOf(
                    TemplateTask(
                        targetHst = 0,
                        careType = CareType.FERTILIZING,
                        title = "Tanam Bibit & Pemupukan Dasar",
                        description = "Pindah bibit sehat umur 21-25 HSS ke lubang tanam sore hari. Siram jenuh perakaran dengan larutan asam humat / vitamin B1 tanaman."
                    ),
                    TemplateTask(
                        targetHst = 7,
                        careType = CareType.FERTILIZING,
                        title = "Pemupukan Susulan 1 (Kocor Starter)",
                        description = "Kocorkan NPK 16-16-16 dosis 3 gram/liter air (200 ml per tanaman). Cek bibit yang mati dan segera lakukan penyulaman."
                    ),
                    TemplateTask(
                        targetHst = 14,
                        careType = CareType.PRUNING,
                        title = "Perompesan Tunas Air (Wiwil 1)",
                        description = "Buang semua tunas air di bawah cabang 'Y' (cabang utama) agar nutrisi terfokus ke pertumbuhan batang atas."
                    ),
                    TemplateTask(
                        targetHst = 15,
                        careType = CareType.PESTICIDE,
                        title = "Penyemprotan Protektif Hama Kutu Daun",
                        description = "Semprot insektisida bahan aktif Abamektin / Imidakloprid untuk cegah kutu kebul dan thrips pembawa virus kuning/gemini."
                    ),
                    TemplateTask(
                        targetHst = 21,
                        careType = CareType.FERTILIZING,
                        title = "Pemupukan Susulan 2 & Pasang Tali Ajir",
                        description = "Kocor NPK 16-16-16 (5 gram/tanaman) + KNO3 Merah untuk pacu anakan cabang produktif. Ikat batang ke ajir bambu."
                    ),
                    TemplateTask(
                        targetHst = 30,
                        careType = CareType.WEEDING,
                        title = "Penyiangan Gulma & Bersihkan Parit",
                        description = "Bersihkan rumput liar di sekitar lubang mulsa/polybag agar tidak berebut unsur hara dan jadi sarang hama."
                    ),
                    TemplateTask(
                        targetHst = 35,
                        careType = CareType.FERTILIZING,
                        title = "Pemupukan Peralihan Generatif",
                        description = "Mulai kocor pupuk tinggi Fosfat & Kalium (MKP + Kalsium) 5 gram/tanaman untuk merangsang kuncup bunga pertama dan mencegah rontok."
                    ),
                    TemplateTask(
                        targetHst = 45,
                        careType = CareType.PESTICIDE,
                        title = "Antisipasi Jamur Antraknosa (Patek)",
                        description = "Semprot fungisida Mankozeb / Azoksistrobin saat buah muda mulai terbentuk, terutama bila sering hujan malam."
                    ),
                    TemplateTask(
                        targetHst = 60,
                        careType = CareType.FERTILIZING,
                        title = "Pemupukan Pengisian & Bobot Buah",
                        description = "Kocor KNO3 Putih + Kalsium Boron dosis 5 gr/pohon untuk mempertebal dinding buah dan menambah rasa pedas & mengkilap."
                    ),
                    TemplateTask(
                        targetHst = 80,
                        careType = CareType.HARVESTING,
                        title = "Panen Perdana Cabai",
                        description = "Petik buah cabai yang mulai merah 80-90% bersama tangkainya pada pagi hari setelah embun kering."
                    ),
                    TemplateTask(
                        targetHst = 87,
                        careType = CareType.HARVESTING,
                        title = "Panen Berkala & Pupuk Pemulihan",
                        description = "Lakukan pemetikan rutin tiap 5-7 hari. Berikan kocor NPK seimbang untuk memperpanjang usia produktif tanaman."
                    )
                )
            ),
            PlantTemplate(
                id = "tomat",
                name = "Tomat Sayur / Buah",
                defaultVariety = "Servo / Gustavi / Tymoti",
                estimatedHarvestHst = 75,
                description = "Template budidaya tomat tahan layu bakteri, pemangkasan daun tua, pemupukan kalsium anti pantat busuk (BER).",
                tasks = listOf(
                    TemplateTask(
                        targetHst = 0,
                        careType = CareType.OTHER,
                        title = "Pindah Tanam & Siram Pangkal",
                        description = "Tanam bibit tomat berdaun 4-5 helai. Padatkan tanah perlahan lalu siram air bersih secukupnya."
                    ),
                    TemplateTask(
                        targetHst = 7,
                        careType = CareType.FERTILIZING,
                        title = "Pemupukan Awal Vegetatif",
                        description = "Kocor NPK 16-16-16 dosis encer (2-3 gr/tanaman) untuk memacu pertumbuhan perakaran baru."
                    ),
                    TemplateTask(
                        targetHst = 14,
                        careType = CareType.PRUNING,
                        title = "Pasang Ajir & Ikat Batang Utama",
                        description = "Tegakkan tanaman dengan ajir setinggi 1,5 meter. Pruning tunas liar di ketiak daun bawah cabang garpu."
                    ),
                    TemplateTask(
                        targetHst = 21,
                        careType = CareType.FERTILIZING,
                        title = "Kocor NPK + Asam Humat",
                        description = "Tingkatkan dosis NPK menjadi 5 gr/tanaman. Semprot pupuk daun mikro penambah klorofil."
                    ),
                    TemplateTask(
                        targetHst = 30,
                        careType = CareType.PESTICIDE,
                        title = "Pengendalian Kutu & Ulat Buah",
                        description = "Semprot insektisida berbahan aktif Spinetoram atau Klorantraniliprol pencegah ulat pembolong buah tomat (Helicoverpa)."
                    ),
                    TemplateTask(
                        targetHst = 40,
                        careType = CareType.FERTILIZING,
                        title = "Aplikasi Kalsium Anti Blossom End Rot",
                        description = "Semprot pupuk kalsium cair tinggi ke seluruh tajuk tanaman untuk mencegah pantat buah tomat hitam/busuk."
                    ),
                    TemplateTask(
                        targetHst = 50,
                        careType = CareType.PRUNING,
                        title = "Perompesan Daun Tua Bawah",
                        description = "Pangkas 3-4 helai daun tertua di pangkal bawah tanah untuk melancarkan sirkulasi udara dan cegah hawar daun."
                    ),
                    TemplateTask(
                        targetHst = 70,
                        careType = CareType.HARVESTING,
                        title = "Panen Perdana Tomat",
                        description = "Petik buah tomat yang semburat oranye kemerahan agar tahan simpan dan tidak memar saat pengangkutan."
                    )
                )
            ),
            PlantTemplate(
                id = "padi",
                name = "Padi Sawah",
                defaultVariety = "Inpari 32 / Ciherang / M400",
                estimatedHarvestHst = 110,
                description = "Template budidaya padi sawah irigasi/tadah hujan, pengaturan air macak-macak, pemupukan berimbang hingga bunting.",
                tasks = listOf(
                    TemplateTask(
                        targetHst = 0,
                        careType = CareType.OTHER,
                        title = "Tanam Pindah (Transplanting)",
                        description = "Tanam bibit umur 15-20 hari, 2-3 batang per rumpun dengan jarak tanam jajar legowo 2:1 atau 4:1."
                    ),
                    TemplateTask(
                        targetHst = 7,
                        careType = CareType.FERTILIZING,
                        title = "Pemupukan Dasar / Susulan 1",
                        description = "Tabur Urea (100 kg/ha) + SP-36 atau NPK Phonska (150 kg/ha). Kondisi sawah macak-macak (sedikit air)."
                    ),
                    TemplateTask(
                        targetHst = 15,
                        careType = CareType.WEEDING,
                        title = "Matun / Gasrok Gulma & Cek Keong",
                        description = "Bersihkan rumput liar menggunakan alat gasrok atau cabut manual. Cek serangan hama keong mas muda."
                    ),
                    TemplateTask(
                        targetHst = 21,
                        careType = CareType.FERTILIZING,
                        title = "Pemupukan Susulan 2 (Fase Anakan Aktif)",
                        description = "Aplikasi Urea (50 kg/ha) + NPK (100 kg/ha) untuk memaksimalkan jumlah anakan produktif."
                    ),
                    TemplateTask(
                        targetHst = 35,
                        careType = CareType.PESTICIDE,
                        title = "Pengendalian Sundep / Penggerek Batang",
                        description = "Monitoring ngengat sundep. Semprot insektisida sistemik berbahan aktif Fipronil atau Dimehipo bila ada gejala mati pucuk."
                    ),
                    TemplateTask(
                        targetHst = 45,
                        careType = CareType.FERTILIZING,
                        title = "Pemupukan Susulan 3 (Fase Primordia / Bunting)",
                        description = "Berikan pupuk Kalium (KCl 50 kg/ha) untuk memperkuat jerami dan memperbanyak calon bulir padi."
                    ),
                    TemplateTask(
                        targetHst = 65,
                        careType = CareType.PESTICIDE,
                        title = "Kawal Fase Keluar Malai & Penyakit Blas",
                        description = "Semprot fungisida Trisiklazol / Difenokonazol saat 10% malai keluar untuk cegah patah leher (blas)."
                    ),
                    TemplateTask(
                        targetHst = 85,
                        careType = CareType.WATERING,
                        title = "Pengeringan Air Sawah",
                        description = "Keringkan parit dan petakan sawah 10-14 hari sebelum panen untuk menyeragamkan kematangan bulir."
                    ),
                    TemplateTask(
                        targetHst = 105,
                        careType = CareType.HARVESTING,
                        title = "Panen Raya Padi",
                        description = "Lakukan pemanenan saat 90-95% bulir padi sudah menguning dan kadar air gabah ideal."
                    )
                )
            ),
            PlantTemplate(
                id = "jagung",
                name = "Jagung (Hibrida / Manis)",
                defaultVariety = "NK 212 / Bonanza / Talenta",
                estimatedHarvestHst = 80,
                description = "Template budidaya jagung hemat tenaga dengan jadwal pembubunan batang, pengawalan ulat FAW dan pengisian tongkol.",
                tasks = listOf(
                    TemplateTask(
                        targetHst = 0,
                        careType = CareType.OTHER,
                        title = "Tugal Benih Jagung",
                        description = "Tanam 1-2 butir per lubang kedalaman 3-5 cm. Tutup dengan bokashi atau tanah halus gembur."
                    ),
                    TemplateTask(
                        targetHst = 10,
                        careType = CareType.FERTILIZING,
                        title = "Pemupukan Pertama (NPK + Urea)",
                        description = "Tugal pupuk berjarak 7-10 cm dari batang jagung. Dosis: NPK 100 kg/ha + Urea 50 kg/ha."
                    ),
                    TemplateTask(
                        targetHst = 18,
                        careType = CareType.PESTICIDE,
                        title = "Pengawasan Ulat Grayak Jagung (FAW)",
                        description = "Periksa pupus daun jagung muda. Semprot insektisida Emamektin benzoat tepat ke dalam corong daun bila ada serbuk gerekan."
                    ),
                    TemplateTask(
                        targetHst = 28,
                        careType = CareType.WEEDING,
                        title = "Pembubunan Batang & Siang Gulma",
                        description = "Timbun tanah gembur ke pangkal batang jagung agar akar jangkar kokoh dan tanaman tidak mudah roboh saat angin kencang."
                    ),
                    TemplateTask(
                        targetHst = 30,
                        careType = CareType.FERTILIZING,
                        title = "Pemupukan Susulan Kedua",
                        description = "Tugal pupuk Urea (100 kg/ha) + NPK (150 kg/ha) berjarak 15 cm dari batang lalu tutup tanah."
                    ),
                    TemplateTask(
                        targetHst = 50,
                        careType = CareType.WATERING,
                        title = "Fase Keluar Bunga & Silking",
                        description = "Pastikan pasokan air tanah cukup. Jangan biarkan tanah kekeringan pada saat penyerbukan tongkol."
                    ),
                    TemplateTask(
                        targetHst = 75,
                        careType = CareType.HARVESTING,
                        title = "Panen Jagung Manis Segar",
                        description = "Panen saat rambut tongkol berwarna cokelat kehitaman dan biji penuh berair manis (untuk jagung manis pipil 70-75 HST, hibrida kering 100-110 HST)."
                    )
                )
            ),
            PlantTemplate(
                id = "bawang",
                name = "Bawang Merah",
                defaultVariety = "Tajuk / Bauji / Bima Brebes",
                estimatedHarvestHst = 60,
                description = "Template budidaya bawang merah intensif: penyiraman harian, pemupukan umbi cepat besar, dan pencegahan ulat grayak.",
                tasks = listOf(
                    TemplateTask(
                        targetHst = 0,
                        careType = CareType.OTHER,
                        title = "Tanam Umbi Bibit Bawang",
                        description = "Potong pucuk umbi bibit 1/3 bagian untuk merangsang tunas serempak. Tancapkan rata tanah pada bedengan."
                    ),
                    TemplateTask(
                        targetHst = 7,
                        careType = CareType.WATERING,
                        title = "Kontrol Siram Pagi & Sore",
                        description = "Jaga kelembapan tanah bedengan. Bilas embun pagi bila daerah sering berkabut untuk mencegah jamur trotol."
                    ),
                    TemplateTask(
                        targetHst = 12,
                        careType = CareType.FERTILIZING,
                        title = "Pemupukan Susulan 1 (NPK + ZA)",
                        description = "Tabur atau cor larutan NPK 16-16-16 + ZA tipis merata di sela tanaman untuk mendukung daun awal."
                    ),
                    TemplateTask(
                        targetHst = 20,
                        careType = CareType.PESTICIDE,
                        title = "Kendalikan Ulat Daun & Penyakit Moler",
                        description = "Semprot insektisida ulat (Klorfenapir) + fungisida tembaga/mankozeb protektif jamur Fusarium."
                    ),
                    TemplateTask(
                        targetHst = 28,
                        careType = CareType.FERTILIZING,
                        title = "Pemupukan Pembentukan Umbi (Fase Generatif)",
                        description = "Gunakan pupuk dominan Kalium (KNO3 Putih / Kalium Sulfat ZK) untuk memperbesar siung bawang dan warna merah menyala."
                    ),
                    TemplateTask(
                        targetHst = 40,
                        careType = CareType.FERTILIZING,
                        title = "Aplikasi Kalium Tambahan & Kalsium",
                        description = "Perkokoh umbi bawang dan daya simpan pasca panen dengan kalsium dan kalium tinggi."
                    ),
                    TemplateTask(
                        targetHst = 50,
                        careType = CareType.WATERING,
                        title = "Penghentian Siram Menjelang Panen",
                        description = "Kurangi drastis intensitas penyiraman agar umbi mengering sempurna dan tidak mudah busuk saat digantung."
                    ),
                    TemplateTask(
                        targetHst = 60,
                        careType = CareType.HARVESTING,
                        title = "Panen Bawang Merah & Penjemuran",
                        description = "Cabut umbi saat 70-80% daun telah rebah kekuningan. Ikat dan jemur di para-para bambu."
                    )
                )
            ),
            PlantTemplate(
                id = "custom",
                name = "Kustom (Tanaman Sendiri)",
                defaultVariety = "Bebas",
                estimatedHarvestHst = 90,
                description = "Buat jadwal perawatan mandiri dengan menentukan target HST sesuai kebutuhan tanaman Anda.",
                tasks = listOf(
                    TemplateTask(
                        targetHst = 0,
                        careType = CareType.OTHER,
                        title = "Tanam Bibit / Sebar Benih",
                        description = "Hari pertama penanaman. Mulai perhitungan HST 0."
                    ),
                    TemplateTask(
                        targetHst = 7,
                        careType = CareType.FERTILIZING,
                        title = "Pemupukan / Nutrisi Awal",
                        description = "Pemberian pupuk awal untuk memicu pertumbuhan tunas dan akar baru."
                    ),
                    TemplateTask(
                        targetHst = 14,
                        careType = CareType.WATERING,
                        title = "Pengecekan Rutin & Pengairan",
                        description = "Cek kelembapan media tanam dan kondisi daun tanaman."
                    ),
                    TemplateTask(
                        targetHst = 30,
                        careType = CareType.FERTILIZING,
                        title = "Pemupukan Lanjutan",
                        description = "Nutrisi tambahan untuk mendukung fase pembentukan cabang dan daun."
                    ),
                    TemplateTask(
                        targetHst = 60,
                        careType = CareType.PESTICIDE,
                        title = "Perlindungan Tanaman",
                        description = "Pembersihan hama dan gulma liar."
                    ),
                    TemplateTask(
                        targetHst = 90,
                        careType = CareType.HARVESTING,
                        title = "Target Panen",
                        description = "Pengecekan kematangan buah/biji dan pemetikan hasil panen."
                    )
                )
            )
        )
    }
}
