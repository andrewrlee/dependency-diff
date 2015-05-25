# dependency-diff

```java

		JarContents version1 = JarContents.load(VERSION_0_0_1_FILE);
		JarContents version2 = JarContents.load(VERSION_0_0_2_FILE);

		Differences differences = version1.difference(version2);
		
		assertThat(differences).additionalClassesAre(
				"com.test.package1.ClassF", 
				"com.test.package2.ClassE", 
				"com.test.package3.ClassG", 
				"com.test.package3.ClassH");
		
		assertThat(differences).removedClassesAre(
				"com.test.package1.ClassG", 
				"com.test.package2.ClassD"); 

```


##TODO
 - Classes
   - New Classes
   - Deleted Classes
   - Altered Classes (Child members altered or class modifiers changed)  
 
 - Constructors
   - New Constructors
   - Removed Constructors
   - Altered Constructors (Different params or modifiers)
 
 - Fields
   - New Fields
   - Removed Fields
   - Altered Fields (modifiers)

 - Static Fields
   - New Fields
   - Removed Fields
   - Altered Fields (modifiers)

 - Methods
   - New Methods
   - Removed Methods
   - Altered methods (signature or modifiers)

 - Static Methods
   - New Methods
   - Removed Methods
   - Altered methods (signature or modifiers)

 - Altered Resources
 
 - Detect moved members.           
 - maven pom dependencies?
