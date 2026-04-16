-- ===================================================================
-- Smart Job Tracker — Seed Data (Jobs Only — Users created via DataInitializer)
-- ===================================================================

-- Sample Jobs
INSERT INTO jobs (title, company, location, description, link, salary, job_type, experience_level, deadline, is_active, created_by, created_at, updated_at)
VALUES
('Senior Java Developer', 'Google', 'Bangalore, India',
 'We are looking for a Senior Java Developer to join our Cloud Platform team. You will design and build scalable microservices using Spring Boot, work with Kubernetes, and collaborate with cross-functional teams to deliver world-class products. Required: Java, Spring.',
 'https://careers.google.com/jobs/java-dev', '25,00,000 - 40,00,000 INR', 'FULL_TIME', 'SENIOR',
 DATEADD('MONTH', 2, CURRENT_DATE), true, 'admin@jobtracker.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Full Stack Engineer', 'Microsoft', 'Hyderabad, India',
 'Join Microsoft Azure team as a Full Stack Engineer. Build enterprise-grade web applications using React and .NET. Experience with CI/CD pipelines and cloud deployment is a plus. Required: React, C#.',
 'https://careers.microsoft.com/fullstack', '20,00,000 - 35,00,000 INR', 'FULL_TIME', 'MID',
 DATEADD('MONTH', 1, CURRENT_DATE), true, 'admin@jobtracker.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Backend Engineering Intern', 'Amazon', 'Chennai, India',
 'Amazon is hiring Backend Engineering Interns for Summer 2025. Work on real-world distributed systems, gain mentorship from senior engineers, and build products used by millions. Must know Java or Python.',
 'https://amazon.jobs/intern-backend', 'Stipend: 80,000/month', 'INTERNSHIP', 'ENTRY',
 DATEADD('MONTH', 1, CURRENT_DATE), true, 'admin@jobtracker.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Frontend Developer Intern', 'Atlassian', 'Remote',
 'Looking for an enthusiastic Frontend Intern to work on Jira. You will be writing modern Javascript, React, and learning robust Frontend architectures.',
 'https://atlassian.com/intern', 'Stipend: 50,000/month', 'INTERNSHIP', 'ENTRY',
 DATEADD('MONTH', 2, CURRENT_DATE), true, 'admin@jobtracker.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Data Science Intern', 'Netflix', 'Remote',
 'Join our recommendations team as a Data Science Intern. You will work with Machine Learning models, analyze massive datasets with Python and Postgres DB.',
 'https://jobs.netflix.com/intern/data', 'Stipend: 100,000/month', 'INTERNSHIP', 'ENTRY',
 DATEADD('MONTH', 3, CURRENT_DATE), true, 'admin@jobtracker.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('DevOps Intern', 'Stripe', 'Bangalore, India',
 'Learn DevOps at a massive scale! We are looking for interns to help manage AWS, Docker, and Kubernetes environments. Great opportunity to learn cloud infrastructure.',
 'https://stripe.com/jobs/intern-devops', 'Stipend: 60,000/month', 'INTERNSHIP', 'ENTRY',
 DATEADD('MONTH', 1, CURRENT_DATE), true, 'admin@jobtracker.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('React Native Intern', 'Uber', 'Hyderabad, India',
 'Help build Uber Eats mobile apps! Looking for a mobile intern eager to learn React Native, iOS, and Android development.',
 'https://uber.com/careers/intern', 'Stipend: 70,000/month', 'INTERNSHIP', 'ENTRY',
 DATEADD('MONTH', 2, CURRENT_DATE), true, 'admin@jobtracker.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Cybersecurity Intern', 'CrowdStrike', 'Pune, India',
 'Do you love breaking things? Intern with our red team to do penetration testing, security audits, and learn modern Cybersecurity practices.',
 'https://crowdstrike.com/intern', 'Stipend: 50,000/month', 'INTERNSHIP', 'ENTRY',
 DATEADD('MONTH', 4, CURRENT_DATE), true, 'admin@jobtracker.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Junior UI/UX Designer', 'Figma', 'Remote',
 'We are looking for a Junior Designer to help craft beautiful tools for other designers. Strong portfolio and understanding of UI/UX required.',
 'https://figma.com/jobs', '$60,000 - $80,000', 'FULL_TIME', 'ENTRY',
 DATEADD('MONTH', 1, CURRENT_DATE), true, 'admin@jobtracker.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Machine Learning Engineer', 'OpenAI', 'San Francisco, USA',
 'Work on cutting-edge AI models at OpenAI. Research and develop large language models, work with massive datasets, and push the boundaries of artificial intelligence. Required: Python, Machine Learning.',
 'https://openai.com/careers/ml-engineer', '$200,000 - $350,000', 'FULL_TIME', 'SENIOR',
 DATEADD('MONTH', 2, CURRENT_DATE), true, 'admin@jobtracker.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Cloud Architect', 'Google', 'Remote',
 'Design robust cloud architectures on GCP for our largest enterprise clients. Required: Cloud, Kubernetes, Docker, System Design.',
 'https://cloud.google.com/careers', '$180,000 - $250,000', 'FULL_TIME', 'SENIOR',
 DATEADD('MONTH', 1, CURRENT_DATE), true, 'admin@jobtracker.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
