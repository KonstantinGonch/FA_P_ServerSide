package universerverside



class DailyJobJob {
    static triggers = {
      cron name: "Trigger", cronExpression: "0 0 18 * * ?"
    }

    def execute() {
        def requests = ActiveRequest.list()
        for (request in requests)
        {
            if (!request.lastDate.isEmpty())
            {
                String lastDateS = request.lastDate
                Date lastDate = new Date().parse("dd.MM.yyyy", lastDateS)
                if (new Date() - 61 > lastDate)
                {
                    ArchiveRequest arch = new ArchiveRequest(request)
                    arch.save(flush : true)
                    request.delete(flush : true )
                }
            }
        }
    }
}
