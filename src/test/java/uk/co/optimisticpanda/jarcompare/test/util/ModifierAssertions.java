package uk.co.optimisticpanda.jarcompare.test.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.SortedSet;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import uk.co.optimisticpanda.jarcompare.util.ModifierUtils;
import uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod;
import uk.co.optimisticpanda.jarcompare.util.data.ModifierTestClass;

public class ModifierAssertions {

	public static ClassPool classPool() throws IOException {
		ClassPool classPool = new ClassPool();
		classPool.makeClass(ModifierAssertions.class.getResourceAsStream(toLocation(ModifierTestClass.class.getName())));
		for (Class<?> class1 : ModifierTestClass.class.getDeclaredClasses()) {
			classPool.makeClass(ModifierAssertions.class.getResourceAsStream(toLocation(class1.getName())));
		}
		return classPool;
	}
	
	private static String toLocation(String name){
		return "/" + name.replaceAll("\\.", "/") + ".class";
	}
	
	public static Verify in(ClassPool classPool){
		return new Verify(classPool);
	}

	public static class Verify{

		private ClassPool classPool;

		public Verify(ClassPool classPool) {
			this.classPool = classPool;
		}
		
		public ModChecker check(String clazzName) throws NotFoundException{
			return new ModChecker(this, classPool.get(clazzName));
		}

		public ModChecker check(Class<?> clazz) throws NotFoundException{
			return check(clazz.getName());
		}
		
		public static class ModChecker{

			private Verify verify;
			private SortedSet<Mod> modifiers;

			public ModChecker(Verify verify, CtClass ctClass) {
				this.verify = verify;
				this.modifiers = ModifierUtils.getModifiers(ctClass);
			}

			public Verify contains(Mod... mods){
				assertThat(modifiers).containsExactly(mods);
				return verify;
			}
		}
	}
	
}
