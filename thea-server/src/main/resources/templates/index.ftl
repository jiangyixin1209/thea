<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>Thea SSO 统一认证中心</title>

    <#import "common/common.macro.ftl" as netCommon>
    <@netCommon.commonStyle />
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <!-- header -->
    <@netCommon.commonHeader />
    <!-- left -->
    <@netCommon.commonLeft "help" />

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">
            <div>
                <h2 style="text-align: center;">
                    Thea分布式单点登录框架
                </h2>
                <h3 style="text-align: center">
                    <a target="_blank" href="https://github.com/xuxueli/xxl-sso">Github</a>&nbsp;&nbsp;&nbsp;&nbsp;
                </h3>
                <h4 style="text-align: center">
                    <iframe src="https://ghbtns.com/github-btn.html?user=jiangyixin1209&repo=thea&type=star&count=true" frameborder="0" scrolling="0" width="170px" height="20px" style="margin-bottom:-5px;"></iframe>
                </h4>
                <br>
            </div>
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

    <!-- footer -->
    <@netCommon.commonFooter />
</div>
<@netCommon.commonScript />
</body>
</html>
