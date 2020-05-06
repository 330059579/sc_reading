/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.beans.factory.xml;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import org.springframework.beans.factory.config.BeanDefinition;
import org.xml.sax.InputSource;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.tests.sample.beans.TestBean;
import org.springframework.util.ObjectUtils;

/**
 * @author Rick Evans
 * @author Juergen Hoeller
 */
public class XmlBeanDefinitionReaderTests extends TestCase {

	public void testSetParserClassSunnyDay() {
		SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
		new XmlBeanDefinitionReader(registry).setDocumentReaderClass(DefaultBeanDefinitionDocumentReader.class);
	}

	public void testSetParserClassToNull() {
		try {
			SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
			new XmlBeanDefinitionReader(registry).setDocumentReaderClass(null);
			fail("Should have thrown IllegalArgumentException (null parserClass)");
		}
		catch (IllegalArgumentException expected) {
		}
	}

	public void testSetParserClassToUnsupportedParserType() throws Exception {
		try {
			SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
			new XmlBeanDefinitionReader(registry).setDocumentReaderClass(String.class);
			fail("Should have thrown IllegalArgumentException (unsupported parserClass)");
		}
		catch (IllegalArgumentException expected) {
		}
	}

	public void testLoader() throws Exception{
		//todo 获取资源
		ClassPathResource resource = new ClassPathResource("org/springframework/beans/factory/xml/bean.xml"); // <1>
		//todo 获取 BeanFactory
		DefaultListableBeanFactory factory = new DefaultListableBeanFactory(); // <2>
		//todo 根据新建的 BeanFactory 创建一个 BeanDefinitionReader 对象，该 Reader 对象为资源的解析器
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory); // <3>
		//todo 装载资源
		reader.loadBeanDefinitions(resource); // <4>
		TestBean kerry = (TestBean)factory.getBean("test3");
		System.out.println(kerry.getAge());
		System.out.println(kerry.getName());
	}

	public void testDemo() throws Exception{
		TestBean bean = new TestBean();
		bean.setAge(4);
		bean.setName("团长");

		TestBean bean1 = new TestBean();
		bean1.setAge(4);
		bean1.setName("团长");
		Set<TestBean> testBeanSet = new HashSet<>();
		boolean add = testBeanSet.add(bean);
		System.out.println(add);
		boolean add1 = testBeanSet.add(bean1);
		System.out.println(add1);
	}

	public void testWithOpenInputStream() {
		try {
			SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
			Resource resource = new InputStreamResource(getClass().getResourceAsStream("test.xml"));
			new XmlBeanDefinitionReader(registry).loadBeanDefinitions(resource);
			fail("Should have thrown BeanDefinitionStoreException (can't determine validation mode)");
		}
		catch (BeanDefinitionStoreException expected) {
		}
	}

	public void testWithOpenInputStreamAndExplicitValidationMode() {
		SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
		Resource resource = new InputStreamResource(getClass().getResourceAsStream("test.xml"));
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(registry);
		reader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_DTD);
		reader.loadBeanDefinitions(resource);
		testBeanDefinitions(registry);
	}

	public void testWithImport() {
		SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
		Resource resource = new ClassPathResource("import.xml", getClass());
		new XmlBeanDefinitionReader(registry).loadBeanDefinitions(resource);
		testBeanDefinitions(registry);
	}

	public void testWithWildcardImport() {
		SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
		Resource resource = new ClassPathResource("importPattern.xml", getClass());
		new XmlBeanDefinitionReader(registry).loadBeanDefinitions(resource);
		testBeanDefinitions(registry);
	}

	public void testWithInputSource() {
		try {
			SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
			InputSource resource = new InputSource(getClass().getResourceAsStream("test.xml"));
			new XmlBeanDefinitionReader(registry).loadBeanDefinitions(resource);
			fail("Should have thrown BeanDefinitionStoreException (can't determine validation mode)");
		}
		catch (BeanDefinitionStoreException expected) {
		}
	}

	public void testWithInputSourceAndExplicitValidationMode() {
		SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
		InputSource resource = new InputSource(getClass().getResourceAsStream("test.xml"));
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(registry);
		reader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_DTD);
		reader.loadBeanDefinitions(resource);
		testBeanDefinitions(registry);
	}

	public void testWithFreshInputStream() {
		SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
		Resource resource = new ClassPathResource("test.xml", getClass());
		new XmlBeanDefinitionReader(registry).loadBeanDefinitions(resource);
		testBeanDefinitions(registry);
	}

	private void testBeanDefinitions(BeanDefinitionRegistry registry) {
		assertEquals(24, registry.getBeanDefinitionCount());
		assertEquals(24, registry.getBeanDefinitionNames().length);
		assertTrue(Arrays.asList(registry.getBeanDefinitionNames()).contains("rod"));
		assertTrue(Arrays.asList(registry.getBeanDefinitionNames()).contains("aliased"));
		assertTrue(registry.containsBeanDefinition("rod"));
		assertTrue(registry.containsBeanDefinition("aliased"));
		assertEquals(TestBean.class.getName(), registry.getBeanDefinition("rod").getBeanClassName());
		assertEquals(TestBean.class.getName(), registry.getBeanDefinition("aliased").getBeanClassName());
		assertTrue(registry.isAlias("youralias"));
		String[] aliases = registry.getAliases("aliased");
		assertEquals(2, aliases.length);
		assertTrue(ObjectUtils.containsElement(aliases, "myalias"));
		assertTrue(ObjectUtils.containsElement(aliases, "youralias"));
	}

	public void testDtdValidationAutodetect() throws Exception {
		doTestValidation("validateWithDtd.xml");
	}

	public void testXsdValidationAutodetect() throws Exception {
		doTestValidation("validateWithXsd.xml");
	}

	private void doTestValidation(String resourceName) throws Exception {
		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		Resource resource = new ClassPathResource(resourceName, getClass());
		new XmlBeanDefinitionReader(factory).loadBeanDefinitions(resource);
		TestBean bean = (TestBean) factory.getBean("testBean");
		assertNotNull(bean);
	}

}
