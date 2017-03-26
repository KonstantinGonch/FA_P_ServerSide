package universerverside

import com.jameskleeh.excel.ExcelBuilder

class CoreController {

    def coreService

    def index() {
        [mes1 : "", stat : "", inquery: "", req : null]
    }

    def findby(String ReqNum)
    {
        if (ReqNum.any() {symbol -> symbol.toLowerCase() in 'a' .. 'z' || symbol.toLowerCase() in 'а'..'я'} || ReqNum.size()==0)
        {
            render (view : "index", model : [mes1: "Ошибка", stat : "Недопустимый формат введенных данных", inquery : "", req : null])
        }
        else {
            def reply = coreService.getState(ReqNum)
            String message = "Статус заявки $ReqNum :"
            if (reply == "NO") {
                render(view: "index", model: [mes1: message, stat: "Заявка с таким номером не была обнаружена", inquery: "", req : null])
            } else {
                render(view: "index", model: [mes1: message, stat: reply, inquery: coreService.getPosition(ReqNum), req : ActiveRequest.findByRegNum(ReqNum)])
            }
        }
    }

    def regNew(String fnum, String snum, String tnum, String loginarg, String passwordarg) {
        def li = Worker.where { login == loginarg }.list()
        if (!(li.size() == 0 || li[0]?.password != passwordarg)) {
            def states = [1: "Заявление зарегистрировано", 2: "Марки изготовлены", 3: "Обязательство принято", 4: "Обеспечение принято", 5: "Получение марок", 6: "Марки получены", 7: "Отчет принят"]
            String number = "$fnum/$snum/$tnum"
            if (ActiveRequest.where { regNum == number }.list().size() > 0) {
                render "Заявка с данным номером была зарегистрирована ${ActiveRequest.findByRegNum(number).regDate}"
            } else {
                int pos = (ActiveRequest.where { state == states[1] }.list().size()) + 1
                ActiveRequest req = new ActiveRequest(number, pos)
                req.regDate = new Date().format("dd.MM.yyyy")
                req.state = states[1]
                req.save(flush: true)
                render "Новая заявка была успешно внесена в базу"
            }
        }
        else
            render "В доступе отказано"
    }

    def getParam(String fnum, String snum, String tnum, String fieldnum)
    {
        String number = "$fnum/$snum/$tnum"
        def fields = ['regNum', 'regDate', 'makeDate', 'accDate', 'position', 'acc2Date', 'getDate', 'get2Date', 'lastDate', 'state', 'position2', 'position3', 'position4']
        def query = ActiveRequest.where {regNum==number}
        if(query.list().size()==0)
        {
            render "Заявка с таким номером не была обнаружена"
        }
        else
        {
            render "${ActiveRequest.findByRegNum(number).getProperty(fields[fieldnum.toInteger()])}"
        }
    }

    def changeState(String fnum, String snum, String tnum, String statenum, String loginarg, String passwordarg)
    {
        def li = Worker.where { login == loginarg }.list()
        if (!(li.size() == 0 || li[0]?.password != passwordarg)) {
            String number = "$fnum/$snum/$tnum"
            def query = ActiveRequest.where { regNum == number }
            if (query.list().size() == 0) {
                render "Заявка с таким номером не была обнаружена"
            } else {
                ActiveRequest req = ActiveRequest.findByRegNum(number)
                String date = new Date().format("dd.MM.yyyy")
                def states = [1: "Заявление зарегистрировано", 2: "Марки изготовлены", 3: "Обязательство принято", 4: "Обеспечение принято", 5: "Получение марок", 6: "Марки получены", 7: "Отчет принят"]
                int cStateInd = 0
                for (i in states.keySet()) {
                    if (states[i] == req.state)
                        cStateInd = i
                }
                if (cStateInd + 1 < statenum.toInteger())
                    render "Несколько промежуточных стадий было пропущено, изменения не внесены"
                else if (cStateInd > statenum.toInteger())
                    render "Попытка отката статуса активной заяки, изменения не внесены"
                else if (cStateInd == statenum.toInteger())
                    render "Текущая заявка уже имеет такой статус, изменения не внесены"
                else {
                    if (statenum.toInteger() == 2) {
                        ActiveRequest.where {
                            position > req.position
                        }.list().each { requ -> requ.position -= 1; requ.save() }
                        req.position = -1
                        req.position2 = ActiveRequest.where { position2 > 0 }.list().size() + 1
                        req.state = states[statenum.toInteger()]
                        req.makeDate = date
                        req.save(flush: true)
                    } else if (statenum.toInteger() == 3) {
                        ActiveRequest.where {
                            position2 > req.position2
                        }.list().each { requ -> requ.position2 -= 1; requ.save() }
                        req.position2 = -1
                        req.position3 = ActiveRequest.where { position3 > 0 }.list().size() + 1
                        req.state = states[3]
                        req.accDate = date
                        req.save(flush: true)
                    } else if (statenum.toInteger() == 4) {
                        ActiveRequest.where {
                            position3 > req.position3
                        }.list().each { requ -> requ.position3 -= 1; requ.save() }
                        req.position3 = -1
                        req.position4 = ActiveRequest.where { position4 > 0 }.list().size() + 1
                        req.state = states[4]
                        req.acc2Date = date
                        req.save(flush: true)
                    } else if (statenum.toInteger() == 5) {
                        ActiveRequest.where {
                            position4 > req.position4
                        }.list().each { requ -> requ.position4 -= 1; requ.save() }
                        req.position4 = -1
                        req.state = states[5]
                        req.getDate = date
                        req.save(flush: true)
                    } else if (statenum.toInteger() == 6) {
                        req.state = states[6]
                        req.get2Date = date
                        req.save(flush: true)
                    } else if (statenum.toInteger() == 7) {
                        req.state = states[7]
                        req.lastDate = date
                        req.save(flush: true)
                    }
                    render "Изменения внесены успешно"
                }
            }
        }
        else
        {render "В доступе отказано"}
    }

    def generateReport(String param, String fdate, String sdate, String loginarg, String passwordarg)
    {
        def li = Worker.where { login == loginarg }.list()
        if (!(li.size() == 0 || li[0]?.password != passwordarg)) {
            Integer parNo = param.toInteger()
            def fields = ['regNum', 'regDate', 'makeDate', 'accDate', 'position', 'acc2Date', 'getDate', 'get2Date', 'lastDate', 'state']
            def fields_t = ['Номер заявления', "Дата регистрации", "Дата информирования об изготовлении марок", "Дата принятия обязательства", "Позиция в очереди на изготовление марок", "Дата принятия обеспечения", "График получения марок", "Дата получения марок", "Дата принятия отчета"]
            def field = fields[parNo]
            Date leftDate = new Date().parse("dd.MM.yyyy", fdate)
            Date rightDate = new Date().parse("dd.MM.yyyy", sdate)
            ArrayList<ActiveRequest> reqs = []
            for (ActiveRequest req in ActiveRequest.list()) {
                try {
                    if (new Date().parse("dd.MM.yyyy", req[field].toString()) >= leftDate && new Date().parse("dd.MM.yyyy", req[field].toString()) <= rightDate)
                        reqs << req
                }
                catch (Exception ex) {
                    //Nothing, pass
                }
            }
            File report = new File("Отчет.xlsx")
            ExcelBuilder.output(new FileOutputStream(report))
                    {
                        sheet
                                {
                                    row
                                            {
                                                cell("Критерий:")
                                                cell("${fields_t[parNo]}")
                                            }
                                    row
                                            {
                                                cell("От:")
                                                cell("${fdate}")
                                                cell("До:")
                                                cell("${sdate}")
                                            }
                                    row
                                            {
                                                cell("Заявок в отчете:")
                                                cell("${reqs.size()}")
                                            }
                                    row
                                            {
                                                cell("Номера заявок: ")
                                                cell("${fields_t[parNo]}")
                                            }
                                    for (ActiveRequest request in reqs) {
                                        row
                                                {
                                                    cell("${request.regNum}")
                                                    cell("${request[field]}")
                                                }
                                    }
                                }
                    }
            response.setContentType("application/excel")
            response.setHeader("Content-disposition", "attachment;filename=report.xlsx")

            response.outputStream << report.newInputStream()
        }
        else
            render "В доступе отказано"
    }

    def newUser(String loginarg, String passwordarg, String loginarg2, String passwordarg2)
    {
        def li = Worker.where { login == loginarg2 }.list()
        if (!(li.size() == 0 || li[0]?.password != passwordarg2)) {
            if (Worker.where { login == loginarg }.list().size() > 0)
                render "Пользователь с таким логином уже зарегистрирован, выберите другой"
            else {
                Worker user = new Worker(login: loginarg, password: passwordarg)
                user.save(flush: true)
                render "Новый пользователь успешно зарегистрирован"
            }
        }
        else
            render "В доступе отказано"
    }

    def getGeneralReport(String loginarg, String passwordarg)
    {
        def li = Worker.where { login == loginarg }.list()
        if (!(li.size() == 0 || li[0]?.password != passwordarg)) {
            File report = new File("Сводка.xlsx")
            ExcelBuilder.output(new FileOutputStream(report))
                    {
                        sheet
                                {
                                    row
                                            {
                                                cell("Регистрационный номер")
                                                cell("Дата регистрации заявления")
                                                cell("Дата информирования об изготовлении марок")
                                                cell("Дата принятия обязательства")
                                                cell("Дата принятия обеспечения")
                                                cell("График получения марок")
                                                cell("Дата закрытия отчета")
                                                cell("Статус заявления")
                                            }
                                    row
                                            {
                                                cell("1")
                                                cell("2")
                                                cell("3")
                                                cell("4")
                                                cell("5")
                                                cell("6")
                                                cell("7")
                                                cell("8")
                                            }
                                    for (ActiveRequest request in ActiveRequest.list())
                                        row
                                                {
                                                    cell("${request.regNum}")
                                                    cell("${request.regDate}")
                                                    cell("${request.makeDate}")
                                                    cell("${request.accDate}")
                                                    cell("${request.acc2Date}")
                                                    cell("${request.getDate}")
                                                    cell("${request.lastDate}")
                                                    cell("${request.state}")
                                                }
                                }
                    }
            response.setContentType("application/excel")
            response.setHeader("Content-disposition", "attachment;filename=general.xlsx")

            response.outputStream << report.newInputStream()
        }
        else
            render "В доступе отказано"
    }

    def enterSystem(String loginarg, String passwordarg)
    {
        def query = Worker.where {login==loginarg}
        def li = query.list()
        if (li.size()==0 || li[0]?.password!=passwordarg)
        {
            render "Неверные данные, доступ невозможен"
        }
        else
        {
            render "OK"
        }
    }
}
