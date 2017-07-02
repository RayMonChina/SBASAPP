using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(TestAndroid.Startup))]
namespace TestAndroid
{
    public partial class Startup {
        public void Configuration(IAppBuilder app) {
            ConfigureAuth(app);
			
        }
    }
}
