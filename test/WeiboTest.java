import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import models.Weibo;

import org.junit.Test;

import com.avaje.ebean.Ebean;


public class WeiboTest {

	@Test
	public void testSaveWeibo() {
		Long lastId = getLastId();
		Weibo wb = new Weibo();
		wb.id = 1L;
		wb.text = "coo";
		wb.source = "voo";
		Weibo.saveWeibo(wb);
		Weibo savedWeibo = Weibo.find.byId(lastId + 1);
        assertThat(savedWeibo.id).isNotNull();
        assertThat(savedWeibo.text).isEqualTo("coo");
        assertThat(savedWeibo.source).isEqualTo("voo");
	}

	@Test
	public void testGetAllWeibo() {
		 List<Weibo> wb = Weibo.find.all();
	     assertThat(wb).isNotNull();
	}

	@Test
	public void testGetPageSize() {
		int size = Weibo.getPageSize();
		assertThat(size).isNotNull();
	}
	@Test
	public void testDel(){
		Long lastId = getLastId();
        int oldCount = Weibo.find.all().size();
        Weibo existingWeibo = Ebean.find(Weibo.class, lastId);
        Ebean.delete(existingWeibo);
        int currentCount = Weibo.find.all().size();
        assertThat(currentCount).isEqualTo(oldCount - 1);
	}
	@Test
	public void testUpdateWeibo(){
		Long lastId = getLastId();

		Weibo existingWeibo = Ebean.find(Weibo.class, lastId);

        String newText = existingWeibo.text + "foo";
        existingWeibo.text = newText;

        Ebean.update(existingWeibo);

        Weibo updatedWeibo = Ebean.find(Weibo.class, lastId);
        assertThat(updatedWeibo.id).isEqualTo(lastId);
        assertThat(updatedWeibo.text).isEqualTo(newText);
	}
	private Long getLastId() {
        return (Long) Weibo.find.orderBy("id desc").findIds().get(0);
    }
}
