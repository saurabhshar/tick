package com.sol.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.sol.utils.Constants;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TickControllerTest {

	@Autowired
	private MockMvc mockMvc;

	String allPrices = "100.73677471151024,106.43611635439936,101.62880319040205,103.38670479070377,106.39163269259085,106.2404390463968,106.81697182084812,102.05549954577923,102.16514795826127,100.53017222031174,108.13700610774801,108.55088335737672,102.91466181602927,109.16603058414026,101.387209102528,104.40946084082239,107.12052569749433,102.07588713698668,109.68332784179027,107.83994440074892,101.2081459646131,104.38009157313179,104.3056810673168,108.19356975540236,105.08006001747383,106.25076018957782,101.58313112370178,108.62444705002518,103.82661224904857,108.73581334065139,105.8264134041653,100.28487959677051,100.12480977012429,103.58517274106926,108.02059760156001,104.61357193808294,104.1884573497628,107.23567625243561,107.49622889533649,103.80004088567195,103.29868497413503,106.54290342197801,109.1209559030843,103.34643326577952,100.92249905813904,106.59225034795254,101.68821824796981,104.79834349549691,106.21799802693648,108.97822984018727,104.12683815280829,101.17911803982345,108.32841975238036,102.3101139495913,104.915119537232,102.43751899294911,106.2831891179016,105.38367986306706,106.6553240379576,103.30651664953055,104.55266452511654,107.45295319105446,102.14916783264219,101.37136356438243,106.34428671586873,109.34249461623332,100.51223438577584,108.85800347160973,101.58135463095898,103.75990410639065,101.87497631838887,107.67824478043109,104.17166657453178,103.09986687066576,107.04024042462036,103.60603576611868,103.65178142980984,108.27520149155445,104.10201174582505,102.10955444572991,109.58366975626468,102.66701375878579,107.47734763683297,105.3267269450308,103.18404082357802,101.15519506234352,106.06322768962778,106.94717145690204,109.582645129583,105.3721527811273,106.30231896009658,109.35146255105958,105.8343725425443,102.81679570499007,102.82641807327913,105.25899400988435,104.35097527618781,109.27243594638229,104.04359780990508,104.34861629478067";
	
	@Test
	public void testConsumeTicks204Response() throws Exception {
		long oldTime = System.currentTimeMillis() - Constants.TICK_LIFE - 100;
		mockMvc.perform(post("/tick").contentType(MediaType.APPLICATION_JSON)
				.content("{\"instrument\": \"GOOGLE\",\"timestamp\": " + oldTime + ", \"price\": 100}"))
				.andExpect(status().is(204));
	}

	@Test
	public void testConsumeTicks201Response() throws Exception {
		long oldTime = System.currentTimeMillis();
		mockMvc.perform(post("/tick").contentType(MediaType.APPLICATION_JSON)
				.content("{\"instrument\": \"AMAZON\",\"timestamp\": " + oldTime + ", \"price\": 100}"))
				.andExpect(status().is(201));
	}

	@Test
	public void testConsumeTicksGetStats() throws Exception {
		long now = System.currentTimeMillis();
		mockMvc.perform(post("/tick").contentType(MediaType.APPLICATION_JSON)
				.content("{\"instrument\": \"IBM\",\"timestamp\": " + now + ", \"price\": 100}"))
				.andExpect(status().is(201));

		mockMvc.perform(get("/statistics/GOOGLE")).andExpect(status().isOk())
				.andExpect(content().json("{\"avg\":0.0,\"max\":0.0,\"min\":0.0,\"count\":0}"));

		mockMvc.perform(post("/tick").contentType(MediaType.APPLICATION_JSON)
				.content("{\"instrument\": \"APPLE\",\"timestamp\": " + (now + 50) + ", \"price\": 200}"))
				.andExpect(status().is(201));

		mockMvc.perform(post("/tick").contentType(MediaType.APPLICATION_JSON)
				.content("{\"instrument\": \"IBM\",\"timestamp\": " + (now + 100) + ", \"price\": 300}"))
				.andExpect(status().is(201));

		mockMvc.perform(get("/statistics")).andExpect(status().isOk());

		mockMvc.perform(get("/statistics/IBM")).andExpect(status().isOk())
				.andExpect(content().json("{\"avg\":200.0,\"max\":300.0,\"min\":100.0,\"count\":2}"));
	}

	/*@Test
	public void testMultipleConsumeTicksGetStats() throws Exception {
		long now = System.currentTimeMillis();
		
		String[] prices = allPrices.split(",");

		for (int i = 0; i < 100; i++) {
			long ts = now + i;
			double price = Double.valueOf(prices[i]);
			mockMvc.perform(post("/tick").contentType(MediaType.APPLICATION_JSON)
					.content("{\"instrument\": \"MULTI_TEST\",\"timestamp\": " + ts + ", \"price\": " + price + "}"))
					.andExpect(status().is(201));
		}
		mockMvc.perform(get("/statistics/MULTI_TEST")).andExpect(status().isOk()).andExpect(content().json(
				"{\"avg\":104.94768899754585,\"max\":109.68332784179027,\"min\":100.12480977012429,\"count\":100}"));
	}
*/
	@Test
	public void testMultipleConsumeTicksGetStatsThreaded() throws Exception {
		long now = System.currentTimeMillis();

		String[] prices = allPrices.split(",");
		ExecutorService es = Executors.newFixedThreadPool(5);
		for (int i = 0; i < 100; i++) {
			long ts = now + i;
			double price = Double.valueOf(prices[i]);
			es.submit(new Runnable() {
				
				@Override
				public void run() {
					try {
						mockMvc.perform(post("/tick").contentType(MediaType.APPLICATION_JSON)
								.content("{\"instrument\": \"MULTI_TEST\",\"timestamp\": " + ts + ", \"price\": " + price + "}"))
								.andExpect(status().is(201));
					} catch (Exception e) {
						e.printStackTrace();
					}					
				}
			});
		}
		
		es.awaitTermination(5, TimeUnit.SECONDS);
		mockMvc.perform(get("/statistics/MULTI_TEST")).andExpect(status().isOk()).andExpect(content().json(
				"{\"avg\":104.94768899754585,\"max\":109.68332784179027,\"min\":100.12480977012429,\"count\":100}"));
	}
	
}
