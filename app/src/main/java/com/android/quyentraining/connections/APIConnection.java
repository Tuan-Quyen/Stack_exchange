package com.android.quyentraining.connections;

import com.android.quyentraining.models.question.ItemQuestionModels;
import com.android.quyentraining.models.places.ItemResultPlace;
import com.android.quyentraining.models.site.ItemSiteModels;
import com.android.quyentraining.models.tag.ItemTagModels;
import com.android.quyentraining.models.user.ItemUserModels;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface APIConnection {
    //site
    @GET("/2.2/sites?pagesize=7&key=mq*Z3A9J)zXCIsTkyU9TQA((")
    Observable<ItemSiteModels> get7Size();
    @GET("/2.2/sites?&key=mq*Z3A9J)zXCIsTkyU9TQA((&pagesize=10")
    Observable<ItemSiteModels> getSitePage(@Query("page") String page);
    //QUESTION
    @GET("/2.2/search?&order=desc&key=mq*Z3A9J)zXCIsTkyU9TQA((&filter=withbody")
    Observable<ItemQuestionModels> getSearchQuestion(@Query("site") String site,@Query("page") String page,@Query("sort") String sort,@Query("intitle") String searchQuestion);
    @GET("/2.2/questions?order=desc&key=mq*Z3A9J)zXCIsTkyU9TQA((&pagesize=30&filter=withbody")
    Observable<ItemQuestionModels> getPageQuestion(@QueryMap Map<String,Object> map/*@Query("site") String site,@Query("page") String page,@Query("sort") String sort*/);
    //tag
    @GET("/2.2/tags?pagesize=15&key=mq*Z3A9J)zXCIsTkyU9TQA((")
    Observable<ItemTagModels> getPageTag(@Query("site") String site, @Query("page") String page, @Query("sort") String sort, @Query("order") String order);
    @GET("/2.2/tags?pagesize=15&key=mq*Z3A9J)zXCIsTkyU9TQA((")
    Observable<ItemTagModels> getSearchTag(@Query("site") String site,@Query("page") String page,@Query("sort") String sort,@Query("order") String order,@Query("inname") String searchTag);
    //user
    @GET("/2.2/users?order=desc&pagesize=30&key=mq*Z3A9J)zXCIsTkyU9TQA((")
    Observable<ItemUserModels> getPageUser(@Query("site") String site,@Query("page") String page,@Query("sort") String sort);
    @GET("/2.2/users?order=desc&pagesize=30&key=mq*Z3A9J)zXCIsTkyU9TQA((")
    Observable<ItemUserModels> getSearchUser(@Query("site") String site,@Query("page") String page,@Query("sort") String sort,@Query("inname") String searchTag);
    //get owner question
    @GET("/2.2/users/{id}?&key=mq*Z3A9J)zXCIsTkyU9TQA((&pagesize=1")
    Observable<ItemUserModels> getOwnerQuestion(@Path("id") String ownerID,@Query("site") String site);
    //get answer question
    @GET("/2.2/questions/{id}/answers?&key=mq*Z3A9J)zXCIsTkyU9TQA((&filter=withbody")
    Observable<ItemQuestionModels> getAnswerQuestion(@Path("id") String ownerID, @Query("site") String site);
    //map api nearby place
    @GET("https://maps.googleapis.com/maps/api/place/nearbysearch/json?sensor=true&key=AIzaSyBBvCBjxq0IkaLq47xpb2LulkI4171faKQ")
    Observable<ItemResultPlace> getNearbyPlace(@Query("type") String type, @Query("location") String latLng, @Query("radius") String radius);
    //map api direction
    /*@GET("https://maps.googleapis.com/maps/api/directions/json?&key=AIzaSyBxBA_hzH3aiIe2KHM4SjI0DR20OgnY_9Q")
    Call<ItemMap> getPolyline(@Query("origin")String latLng1, @Query("destination") String latLng2);*/
}
