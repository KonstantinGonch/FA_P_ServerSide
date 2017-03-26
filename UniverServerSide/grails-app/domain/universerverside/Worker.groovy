package universerverside

class Worker {

    String login
    String password

    static constraints = {
        login blank : false, nullable : false
        password blank : false, nullable : false
    }
}
