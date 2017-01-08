namespace SampleProject.Model
{
    public class PersonalInfo {
        public PersonalInfo(string userId)
        {
            this.UserId = userId;
        }
        
        public string UserId { get; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string Email { get; set; }
        public bool IsEmployed { get; set; }
    }
}