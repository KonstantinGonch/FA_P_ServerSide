package universerverside

class ActiveRequest {

    String regNum //Регистрационный номер
    String regDate = null //Дата регистрации
    String makeDate = null //Дата информирования об изготовлении марок
    String accDate = null //Дата принятия обязательства
    Integer position = null //Позиция в очереди на изготовление марок
    Integer position2 = null //Очередь на принятие обязательства
    Integer position3 = null //Очередь на принятие обеспечения
    Integer position4 = null //Очередь на получение марок
    String acc2Date = null //Дата принятия обеспечения
    String getDate = null //Дата (график) получения марок
    String get2Date = null //Дата забора марок
    String lastDate = null //Дата принятия отчета
    String state = null //Текущий статус

    ActiveRequest(String numa, Integer pos)
    {
        regNum = numa; regDate = ""; makeDate = ""; accDate = ""; position = pos; acc2Date = ""
        getDate = ""; get2Date = ""; lastDate = ""; state = ""
        position2 = -1; position3 = -1; position4 = -1
    }

    static constraints = {
        regNum blank : false, nullable : false, unique : true
    }
}
