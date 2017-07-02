using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Optimization;
using System.Web.Routing;
using System.Web.Security;
using System.Web.SessionState;
using SBASApi.BLL;
namespace SBASApi
{
    public class Global : HttpApplication
    {
        void Application_Start(object sender, EventArgs e)
        {
            // 在应用程序启动时运行的代码
        }
        void Application_Error(object sender, EventArgs e)
        {

            Exception ex = Server.GetLastError();
            SimpleLog.WriteErrorLog(ex.Message + "\r\n" + ex.StackTrace);
        }
    }
}