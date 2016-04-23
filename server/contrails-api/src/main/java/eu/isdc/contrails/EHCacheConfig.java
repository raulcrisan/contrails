package eu.isdc.contrails;

@Configuration
public class EHCacheConfig {

	@Bean 
	public EGCacheConfig() {
		CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
		          .withCache("preConfigured",
		               CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
		                                              ResourcePoolsBuilder.heap(100))
		               .build())
		          .build(true);

		      Cache<Long, String> preConfigured
		          = cacheManager.getCache("preConfigured", Long.class, String.class);

		      Cache<Long, String> myCache = cacheManager.createCache("myCache",
		          CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
		                                        ResourcePoolsBuilder.heap(100)).build());

	}


}
