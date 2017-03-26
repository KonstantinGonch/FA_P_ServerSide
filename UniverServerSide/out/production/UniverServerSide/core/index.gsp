<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Введите Данные</title>
</head>
<body style="background-image:url('${resource(dir: "images/", file: "backgr.png")}'); background-size: cover; background-repeat: no-repeat;">
    <h1 style="color: #bce8f1; text-align: center;">Введите необходимые данные:</h1>
    <formset>
        <form action = "findby" style = "text-align: center; margin-top: 50px">
            <label for = "ReqNum" style="color: #bce8f1">Введите номер заявки</label>
            <g:textField name="ReqNum"/>
            <g:submitButton name = "search" value = "OK"/>
        </form>
    </formset>
    <div style="background-color: #252327; opacity: 0.7; text-align: center; margin-top: 50px; height: 500px; margin-left: 50px; margin-right: 50px">
        <h2 style = "color:#7DF9FF ">Отчет</h2>
        <h1 style = "color:#7DF9FF; margin-top: 50px ">${mes1}</h1>
        <h1 style = "color:#7DF9FF; margin-top: 50px ">${stat}</h1>
    </div>
</body>
</html>