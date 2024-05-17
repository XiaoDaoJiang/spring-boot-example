<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
</head>
<body>
<div style="display: flex;flex-direction: column;gap: 16px;padding-left: 30px;padding-top: 50px">
    <h3>结果</h3>
    <div style="width: 100%;height: 1px; background: #000;"></div>
    <div style="width: 100%;height: 16px;"></div>
    <table style="width: 100%;table-layout: fixed;border-collapse: collapse;">
        <colgroup>
            <col style="width: 40%;"/>
            <col style="width: 30%;min-width: 150px;"/>
            <col style="width: 30%;min-width: 100px;"/>
        </colgroup>
        <thead>
        <tr>
            <th id="1" style="width: 50%; border-width: 1px;border-style: solid none none solid; padding: 8px;white-space: nowrap; text-align: center;">
                项目
            </th>
            <th id="1" style="width: 10%;  border-width: 1px;border-style: solid none none solid; padding: 8px; text-align: center;">总分</th>
            <th id="2" style="width: 10%;  border-width: 1px;border-style: solid solid none solid; padding: 8px; text-align: center;">得分</th>
        </tr>
        </thead>
        <tbody>
        <#list ts as t>
            <tr>
                <#if t?has_next>
                    <td style="border-width: 1px;border-style: solid none none solid; padding: 8px; text-align: center;">${1}</td>
                    <td style="border-width: 1px;border-style: solid none none solid; padding: 8px; text-align: center;">${2}</td>
                    <td style="border-width: 1px;border-style: solid solid none solid; padding: 8px; text-align: center;">${3}</td>
                <#else>
                    <td style="border-width: 1px;border-style: solid none solid solid; padding: 8px; text-align: center;">${1}</td>
                    <td style="border-width: 1px;border-style: solid none solid solid; padding: 8px; text-align: center;">${2}</td>
                    <td style="border-width: 1px;border-style: solid; padding: 8px; text-align: center;">${3}</td>
                </#if>
            </tr>
        </#list>
        </tbody>
    </table>
</div>
</body>
</html>