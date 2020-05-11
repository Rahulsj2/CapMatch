# CapMatch
Capstone Matching System for Seniors 


SOFTWARE FUNCTIONS: MEETING REQUIREMENTS

Sign up
  - First name, last name, email, password, major, add interests, pick SDGs
Log in
  - email, password
Students add top 3 faculty choices
Faculty select 3 preferred students
Matching
  - Students to faculty
  - Faculty to student
Search
  - For faculty
  - For students



DATA STRUCTURES

The backend of our application does not implement any particular data structure, but is based on the Java Collections library. The collection models used include HashSets, HashMaps, TreeSets, and TreeMaps, ArrayLists, etc.
These can be seen across all our object models which can be found in the Models subfolder of the backend directory, as well as services such as our EmailService and DataValidationService.


-----------------------------------------------------------------------------------------------------

UI/UX DESIGN

We used Adobe XD to create high-fidelity mockups of the various screens and potential user interactions for students, faculty and admins. This helped us to better visualize the user flows and iterate as needed.



FRONTEND

Vue.js: Vue.js is a JavaScript frontend framework for developing interactive web interfaces. It mainly focuses on the view layer which can be easily integrated into larger projects. It makes use of a virtual Document Object Model (DOM) which makes web page responses faster and also makes the view dynamic, making it easy to scale with its data binding, components, event handling, templates and computed properties. It also includes end-to-end testing for evaluating Vue components and ensuring the system works as expected from start to finish.

Vuelidate: Vuelidate is a data model oriented Vue library for form or input validations which are added to the component definition through validation objects. This helps ensure all data collected from users are correct avoiding any errors and preventing intentional acts to attack the system.
Bootstrap: We also used the bootstrap CSS framework to help build a more responsive system alongside the Vue to make the frontend more dynamic.
Jest: Jest is a javascript testing framework which was used for unit testing Vue components to ensure all individual components worked as they should before integrating them into the larger project.



BACKEND

Java Spring Boot: Spring Boot is a popular framework for building Enterprise Applications. One of the goals for our project was to build a project that is modular and maintainable for the long term. Choosing an Enterprise framework that has been highly supported for two decades is great for longevity.


Apache Freemarker: Freemarker is the templating engine of choice for many Java developers. Due to its ease of use and extensive support from Apache, Freemarker is a stable choice of templating engine we can be confident about for the long term.


Java Mail Sender: The Java Mail Sender interface provides support for sending and processing emails. Due to its wide array of functionalities, it was a great choice for building an email service that would scale well. For example, although we do not currently make use 

Lombok: Lombok is a tool that enables Spring developers to skip creating routine methods such as getters and setters, equals and hashcode, constructors and many more. Lombok allows developers to keep Java code succinct by automatically generating all needed methods at runtime.
