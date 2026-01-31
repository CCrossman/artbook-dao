package com.artbook.dao.integration.base;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <b>Integration Test Base</b><br>
 * You can create more than one Integration Test Base class for different
 * purpose
 * 
 * @author Wenbo Wang (jackie-1685@163.com)
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes =
// TestScopeWebSecurityConfigurer.class)
@Ignore
public class IntegrationTestBase {

}
