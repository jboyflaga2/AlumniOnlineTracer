using AlumniOnlineTracer.Core.Interfaces;
using AlumniOnlineTracer.Data;
using AlumniOnlineTracer.WebAPI.Data;
using AlumniOnlineTracer.WebAPI.Services;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using Microsoft.EntityFrameworkCore;
using AlumniOnlineTracer.WebAPI.Models.AuthenticationViewModels;
using AlumniOnlineTracer.WebAPI.Infrastructure;

namespace AlumniOnlineTracer.WebAPI
{
	public class Startup
	{
		public Startup(IHostingEnvironment env)
		{
			var builder = new ConfigurationBuilder()
				.SetBasePath(env.ContentRootPath)
				.AddJsonFile("appsettings.json", optional: true, reloadOnChange: true)
				.AddJsonFile($"appsettings.{env.EnvironmentName}.json", optional: true)
				.AddEnvironmentVariables();
			Configuration = builder.Build();
		}

		public IConfigurationRoot Configuration { get; }

		// This method gets called by the runtime. Use this method to add services to the container.
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


			// Add framework services.
			services.AddMvc();

			services.AddScoped<IAlumniRepository, AlumniRepository>();
			services.AddScoped<AlumniService>();


		}

		// This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
		public void Configure(IApplicationBuilder app, IHostingEnvironment env, ILoggerFactory loggerFactory)
		{
			loggerFactory.AddConsole(Configuration.GetSection("Logging"));
			loggerFactory.AddDebug();


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
	}
}
