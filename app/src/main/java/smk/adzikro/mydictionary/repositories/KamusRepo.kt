package smk.adzikro.mydictionary.repositories

import smk.adzikro.mydictionary.models.Kamus

class KamusRepo(val dataSource: DataSource) {
    fun addKamus(kamus:MutableList<Kamus>) = dataSource.addKamus(kamus)
    fun getKamus(kata:String) = dataSource.getKamus("""%$kata%""")
    fun getSuggest(kata:String) = dataSource.getKamus("""$kata%""")
}