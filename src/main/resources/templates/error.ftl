<!DOCTYPE html>
<html lang="zh">
<body>
<h1>系统异常统一处理</h1>
<h3>${errorMessage}</h3>
<h2>Debug</h2>

<a href="https://www.google.com/webhp?hl=zh-CN#safe=strict&hl=zh-CN&q=${errorMessage}"
   class="btn btn-primary btn-lg" target="_blank" id="Google">Google</a>

<a href="https://www.baidu.com/s?wd=${errorMessage}" class="btn btn-info btn-lg" target="_blank" id="Baidu">Baidu</a>

<a href="http://stackoverflow.com/search?q=${errorMessage}"
   class="btn btn-default btn-lg" target="_blank" id="StackOverFlow">StackOverFlow</a>

<h2>异常堆栈跟踪日志StackTrace</h2>
<code>
<#list stackTrace as st>
   ${st}
</#list>
</code>
</body>

<script>
    (function () {
        var ie = !!(window.attachEvent && !window.opera);
        var wk = /webkit\/(\d+)/i.test(navigator.userAgent) && (RegExp.$1 < 5);
        var fn = [];
        var run = function () {
            for (var i = 0; i < fn.length; i++) fn[i]();
        };

        var d = document;
        d.ready = function (f) {
            if (!ie && !wk && d.addEventListener)
                return d.addEventListener('DOMContentLoaded', f, false);
            if (fn.push(f) > 1) return;
            if (ie)
                (function () {
                    try {
                        d.documentElement.doScroll('left');
                        run();
                    }
                    catch (err) {
                        setTimeout(arguments.callee, 0);
                    }
                })();
            else if (wk)
                var t = setInterval(function () {
                    if (/^(loaded|complete)$/.test(d.readyState))
                        clearInterval(t), run();
                }, 0);
        };
    })();

    document.ready(function () {
        document.getElementById("Google").click();
        document.getElementById("Baidu").click();
        document.getElementById("StackOverFlow").click();
    })


</script>

</html>
