package universerverside

class ArchiveRequest {

    String regNum //Регистрационный номер
    String regDate = null //Дата регистрации
    String makeDate = null //Дата информирования об изготовлении марок
    String accDate = null //Дата принятия обязательства
    String acc2Date = null //Дата принятия обеспечения
    String getDate = null //Дата (график) получения марок
    String get2Date = null //Дата забора марок
    String lastDate = null //Дата принятия отчета
    String state = null //Текущий статус

    ArchiveRequest(ActiveRequest req)
    {
        regNum = req.regNum; regDate = req.regDate; makeDate = req.makeDate
        accDate = req.accDate; acc2Date = req.acc2Date
        getDate = req.getDate; get2Date = req.get2Date; lastDate = req.lastDate; state = "Архивировано"
    }

    static constraints = {
        regNum blank : false, nullable : false
    }
}
