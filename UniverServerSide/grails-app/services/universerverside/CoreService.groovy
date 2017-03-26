package universerverside

import grails.transaction.Transactional

@Transactional
class CoreService {

    def getState(String num) {
        def query = ActiveRequest.where {regNum==num}
        if (query.list().size()==0)
            return "NO"
        else
            return query.list()[0].state
    }

    def getPosition(String num)
    {
        ActiveRequest request = ActiveRequest.findByRegNum(num)
        if (request.position==-1)
        {
            if(request.position2==-1)
            {
                if (request.position3==-1)
                {
                    if (request.position4==-1)
                    {
                        return "В очереди нет"
                    }
                    else
                    {
                        return "В очереди на получение марок : ${request.position4}"
                    }
                }
                else
                {
                    return "В очереди на принятие обеспечения : ${request.position3}"
                }
            }
            else
            {
                return "В очереди на принятие обязательства : ${request.position2}"
            }
        }
        else
        {
            return "В очереди на изготовление марок : ${request.position}"
        }
    }
}
