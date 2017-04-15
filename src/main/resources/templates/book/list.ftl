<!DOCTYPE html>
<html lang="zh">
<body>
<br>
<div>
<#list books as book>
    <p></p>
    <li>书名： ${book.name}</li>
    <li>作者： ${book.author}</li>
    <li>出版社： ${book.press}</li>
    <li>借出时间： ${book.outDate?string('yyyy/MM/dd HH:mm:ss')}</li>
    <li>还书时间： ${book.inDate?string('yyyy/MM/dd HH:mm:ss')}</li>
    <li>状态： ${book.state}</li>
</#list>
</div>
</body>

</html>
