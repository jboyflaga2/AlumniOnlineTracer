using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.Configuration;

namespace AlumniOnlineTracer.WebAPI
{
	public class Program
	{
		public static void Main(string[] args)
		{
			// "How to specify the port an ASP.NET Core application is hosted on"
			// - from http://stackoverflow.com/questions/37365277/how-to-specify-the-port-an-asp-net-core-application-is-hosted-on
			//var configuration = new ConfigurationBuilder()
			//	.AddCommandLine(args)
			//	.Build();
			// // > dotnet run --server.urls=http://localhost:5001/

			// "How to configure Kestrel URLs in ASP.NET Core"
			// - from http://benfoster.io/blog/how-to-configure-kestrel-urls-in-aspnet-core-rc2
			var configuration = new ConfigurationBuilder()
				.SetBasePath(Directory.GetCurrentDirectory())
				.AddJsonFile("hosting.json", optional: true)
				.Build();

			var host = new WebHostBuilder()
				.UseKestrel()
				.UseConfiguration(configuration)
				.UseContentRoot(Directory.GetCurrentDirectory())
				.UseIISIntegration()
				.UseStartup<Startup>()
				//.UseUrls("http://localhost:5001/")
				.Build();

			host.Run();
		}
	}
}
