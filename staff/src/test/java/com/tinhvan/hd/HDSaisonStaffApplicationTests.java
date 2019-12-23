package com.tinhvan.hd;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HDSaisonStaffApplicationTests {
    String url = "http://localhost:8808/api/v1/staff/list";
    String url1 = "http://192.168.75.198:8807/api/v1/role/find";

    @Test
    public void contextLoads() {
    }

//
//	@Test
//	public void list() {
//		Invoker invoker = new Invoker();
//		StaffSetRole staffSetRole = new StaffSetRole();
//		staffSetRole.setRole(1000);
//		staffSetRole.setUuid(UUID.fromString("5f00b3dd-5872-4a88-80f7-e6adee8ca004"));
//		invoker.call(url1, staffSetRole, new ParameterizedTypeReference() {});
//		ResponseDTO<Role> rs = invoker.call(url1, staffSetRole, new ParameterizedTypeReference<ResponseDTO<Role>>() {});
//		Assert.assertEquals(200, rs.getCode());


//		RestTemplate restTemplate = new RestTemplate();
//		HttpHeaders headers = new HttpHeaders();
//		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//		headers.setBearerAuth("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2QzM2I3MzQtOTFiMC00M2NmLTg2NjEtM2RhZmM1ZDMyMTEzIiwicm9sZSI6MTAwMCwiZXhwaXJlZEF0IjoxNTY1NDExMjM3MDAwfQ.LVpYFvLgewTleC8HB5bRNcuDJ8hYUzf3aWbxeI5Ojeo");
//		List<LanguageRange> list = new ArrayList<>();
//		list.add(new LanguageRange("vi"));
//		headers.setAcceptLanguage(list);
//		headers.set("x-api-key","aa86719bb53d3a8fc470210d7e7a1b4388da4fa2");
//		HttpEntity<StaffSetRole> entity = new HttpEntity<>(staffSetRole,headers);
//		//find role on authorize
//		String url = "http://192.168.75.198:8807/api/v1/role/find";
//
//		Invoker invoker = new Invoker();
//		ResponseDTO<Role> rs = invoker.call(url, staffSetRole, new ParameterizedTypeReference<ResponseDTO<Role>>() {
//		});
//
////		restTemplate.exchange(url1, HttpMethod.POST, entity, Role.class);
//
//		ResponseEntity<Role> result = restTemplate.exchange(url1, HttpMethod.POST, entity, Role.class);

//		System.out.println(result);


//		Role role = restTemplate.postForObject(url1, entity, Role.class);
//		ResponseEntity<Role> response
//				= restTemplate.getForEntity(url1, Role.class);
//		HttpEntity<Role> request = new HttpEntity<>(new Role());
//		Role foo = restTemplate.postForObject(url1, entity, Role.class);
//
//
//		restTemplate.exchange(url, HttpMethod.POST, entity, String.class);


//	}

//	@Test
//	public void testAddEmployeeWithoutHeader_success() throws URISyntaxException
//	{
//		RestTemplate restTemplate = new RestTemplate();
//		StaffSetRole staffSetRole = new StaffSetRole();
//		staffSetRole.setRole(1000);
//		staffSetRole.setUuid(UUID.fromString("5f00b3dd-5872-4a88-80f7-e6adee8ca004"));
//
//
//
//		HttpHeaders headers = new HttpHeaders();
//		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//		headers.setBearerAuth("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2QzM2I3MzQtOTFiMC00M2NmLTg2NjEtM2RhZmM1ZDMyMTEzIiwicm9sZSI6MTAwMCwiZXhwaXJlZEF0IjoxNTY1NDExMjM3MDAwfQ.LVpYFvLgewTleC8HB5bRNcuDJ8hYUzf3aWbxeI5Ojeo");
//		List<LanguageRange> list = new ArrayList<>();
//		list.add(new LanguageRange("vi"));
//		headers.setAcceptLanguage(list);
//		headers.set("x-api-key","aa86719bb53d3a8fc470210d7e7a1b4388da4fa2");
//
//		HttpEntity<StaffSetRole> entity = new HttpEntity<>(staffSetRole,headers);
//
////		RestTemplate restTemplate = new RestTemplate();
//		ResponseEntity<?> responseObj = restTemplate.exchange(url1, HttpMethod.POST,entity,Role.class);
//		Role resObj = (Role) responseObj.getBody();

//		final String baseUrl = "http://localhost:"+randomServerPort+"/employees/";
//		URI uri = new URI(baseUrl);
//
////		Employee employee = new Employee(null, "Adam", "Gilly", "test@email.com");
//
//		ResponseEntity<?> result = restTemplate.postForEntity(uri, ResponseDTO<Role>, Role.class);

    //Verify request succeed
//		Assert.assertEquals(201, result.getStatusCodeValue());
//	}

}
