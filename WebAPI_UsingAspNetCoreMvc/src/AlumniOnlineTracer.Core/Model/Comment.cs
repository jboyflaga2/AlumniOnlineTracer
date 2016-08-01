using System;

namespace AlumniOnlineTracer.Core.Model
{
    public class Comment
    {
		public Guid Id { get; set; }
		public DateTime DateCreated { get; set; }
		public DateTime DateLastModified { get; set; }
		
		public string Content { get; set; }
	}
}
