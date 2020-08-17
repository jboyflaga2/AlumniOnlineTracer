# AlumniOnlineTracer
Alumni Online Tracer using .NET Core, Angular2, and amCharts


## NOTES on how to run this project (August 18, 2020)

1. You need to [download and install Visual Studio Community 2015](https://visualstudio.microsoft.com/vs/older-downloads/) (refer to https://stackoverflow.com/a/43680545/1451757)


2. To solve error `The imported project "C:\Program Files (x86)\MSBuild\Microsoft\VisualStudio\v14.0\DotNet\Microsoft.DotNet.Props" was not found.`, [install Visual Studio 2015 Tools (Preview 2)](https://stackoverflow.com/a/40955825/1451757) (Direct Download: https://go.microsoft.com/fwlink/?LinkId=827546)

3. To solve he [Missing SDK error](https://github.com/aspnet/Tooling/blob/master/known-issues-vs2015.md#missing-sdk) 

[Where to download previous version of .net core sdk?](https://stackoverflow.com/a/40718455/1451757)

	1. download zip file for [.NET Core SDK](https://dotnet.microsoft.com/download/dotnet-core/1.0) 1.0.0-preview2.1-003177 (https://dotnet.microsoft.com/download/dotnet-core/thank-you/sdk-1.0.0-preview2-windows-x64-binaries)

	2. copy `1.0.0-preview2-1-003177` directory inside `dotnet-dev-win-x64.1.0.0-preview2-1-003177\sdk`

	3. paste in `C:\Program Files\dotnet\sdk`

4. Error regarding OpenIddict is not yet resolved


## Some Useful Links
* Exporting charts and maps: PDF with multiple charts and related info - https://www.amcharts.com/tutorials/exporting-pdf-with-multiple-charts-and-related-info/

### Change port in an ASP.NET Core application
 - http://stackoverflow.com/questions/37365277/how-to-specify-the-port-an-asp-net-core-application-is-hosted-on
 - http://benfoster.io/blog/how-to-configure-kestrel-urls-in-aspnet-core-rc2
 
### JSON serialization/deserialization
 - http://stackoverflow.com/questions/29841503/json-serialization-deserialization-in-vnext
 
### Calling Web API in .NET Core
 - https://github.com/dotnet/corefx/issues/9213
 - http://www.dotnetperls.com/httpclient

 
### Security

#### Token Authentication
 - https://scotch.io/tutorials/the-ins-and-outs-of-token-based-authentication
 - https://stormpath.com/blog/token-authentication-scalable-user-mgmt
 - https://stormpath.com/blog/token-authentication-asp-net-core
 
#### Token Authentication - OpenIdDict
 - http://capesean.co.za/blog/asp-net-5-jwt-tokens/
 - https://github.com/openiddict/openiddict-core/
 - https://github.com/openiddict/openiddict-core/samples
 - https://github.com/aspnet/Security/tree/dev/samples
 - https://github.com/devilsuraj/openiddict-samples/tree/master/Samples/resource-owner-password-credential/Angualar2-Client-ROPC
 
#### JWT vs OAuth
 - http://programmers.stackexchange.com/questions/298973/rest-api-security-stored-token-vs-jwt-vs-oauth
 - http://www.seedbox.com/en/blog/2015/06/05/oauth-2-vs-json-web-tokens-comment-securiser-un-api/

#### The Ultimate Guide to Mobile API Security - https://stormpath.com/blog/the-ultimate-guide-to-mobile-api-security
 
 
#### Using JWT with Facebook or google login (https://www.google.com.ph/#q=jwt+with+facebook+or+google+login)
 - Social sign in with single-page app and JWT server validation - https://ole.michelsen.dk/blog/social-signin-spa-jwt-server.html (look at the diagram)

![Token Authentication diagram](https://ole.michelsen.dk/images/blog/social-signin-spa-jwt-server/token-authentication.png)
 
 

 
#### Other
 - http://bitoftech.net/2014/08/11/asp-net-web-api-2-external-logins-social-logins-facebook-google-angularjs-app/
 - http://nordicapis.com/api-security-oauth-openid-connect-depth/
 
 
## Steps I took when adding OpenIddict (following the steps in http://capesean.co.za/blog/asp-net-5-jwt-tokens/)

### Add nuget.config file
```
<?xml version="1.0" encoding="utf-8"?>
<configuration>
  <packageSources>
    <add key="NuGet" value="https://api.nuget.org/v3/index.json" />
    <add key="aspnet-contrib" value="https://www.myget.org/F/aspnet-contrib/api/v3/index.json" />
  </packageSources>
</configuration>
```

### In appsettings.json
```
	"Data": {
		"AuthenticationDBConnection": {
			"ConnectionString": "Server=(localdb)\\MSSQLLocalDB;Database=AuthenticationDB;Trusted_Connection=True;MultipleActiveResultSets=true"
		}
	},
```

### In the dependencies of project.json add OpenIddict...
```
    "OpenIddict": "1.0.0-*",
	"Microsoft.AspNetCore.Authentication.JwtBearer": "1.0.0",
	"Microsoft.EntityFrameworkCore.SqlServer": "1.0.0"
```

### Add the User model; create ApplicationUser.cs file
```
using OpenIddict;
namespace ....Models
{
    // Add profile data for application users by adding properties to the ApplicationUser class
    public class ApplicationUser : OpenIddictUser
    {
        public string GivenName { get; set; }
    }
}
```

### Create ApplicationRole.cs file
```
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;

namespace AlumniOnlineTracer.WebAPI.Models
{
	public class ApplicationRole : IdentityRole
	{
	}
}
```

### Create ApplicationDbContext.cs file
```
using AlumniOnlineTracer.WebAPI.Models.AuthenticationViewModels;
using Microsoft.EntityFrameworkCore;
using OpenIddict;

namespace AlumniOnlineTracer.WebAPI.Data
{
	public class AuthenticationDbContext : OpenIddictDbContext<ApplicationUser, ApplicationRole>
	{
		public AuthenticationDbContext(DbContextOptions options) : base(options)
		{ }

		protected override void OnModelCreating(ModelBuilder builder)
		{
			base.OnModelCreating(builder);
		}
	}
}
```

### In Startup.cs
```
public void ConfigureServices(IServiceCollection services)
{
	// add entity framework using the config connection string
	services.AddEntityFrameworkSqlServer()
		.AddDbContext<AuthenticationDbContext>(options =>
			options.UseSqlServer(Configuration["Data:AuthenticationDBConnection:ConnectionString"]));

	// add identity
	services.AddIdentity<ApplicationUser, ApplicationRole>()
		.AddEntityFrameworkStores<AuthenticationDbContext>()
		.AddUserManager<CustomOpenIddictManager>()
		.AddDefaultTokenProviders();

	// add OpenIddict
	services.AddOpenIddict<ApplicationUser, ApplicationRole, AuthenticationDbContext>()
		.DisableHttpsRequirement()
		.EnableTokenEndpoint("/connect/token")
		.AllowPasswordFlow()
		.AllowRefreshTokenFlow()
		.UseJsonWebTokens()
		.AddEphemeralSigningKey();

	...
	
}
```

```
public void Configure(IApplicationBuilder app, IHostingEnvironment env, ILoggerFactory loggerFactory)
{
	...
	
	// don't use identity as this is a wrapper for using cookies, not needed
	//app.UseIdentity();

	app.UseOpenIddict();

	// use jwt bearer authentication
	app.UseJwtBearerAuthentication(new JwtBearerOptions
	{
		AutomaticAuthenticate = true,
		AutomaticChallenge = true,
		RequireHttpsMetadata = false,
		Audience = "http://localhost:58292/",
		Authority = "http://localhost:58292/"
	});

	app.UseMvc();
}
```
