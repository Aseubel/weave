package com.aseubel.weave;

import com.aseubel.weave.pojo.entity.ich.Category;
import com.aseubel.weave.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 非遗分类测试类 - 初始化示例数据
 * @author Aseubel
 * @date 2025/7/10 上午10:44
 */
//@SpringBootTest
public class CategoryTest {
    
//    @Autowired
    private CategoryRepository categoryRepository;
    
    /**
     * 初始化非遗分类数据
     * 包括十大类及其部分二级分类
     */
//    @Test
//    @Transactional
//    @Commit
    public void initIchCategories() {
        // 清空现有数据
        categoryRepository.deleteAll();
        
        // 创建非遗十大类（顶级分类）
        List<Category> topCategories = Arrays.asList(
            Category.builder()
                .name("民间文学")
                .description("包括神话、传说、民间故事、民间歌谣、谚语、谜语等各种形式的民间文学作品")
                .parentId(null)
                .level(1)
                .code("FL")
                .sortOrder(1)
                .build(),
            Category.builder()
                .name("传统音乐")
                .description("包括民间音乐、传统声乐、传统器乐等音乐形式")
                .parentId(null)
                .level(1)
                .code("TM")
                .sortOrder(2)
                .build(),
            Category.builder()
                .name("传统舞蹈")
                .description("包括民间舞蹈、宗教舞蹈、宫廷舞蹈等各种传统舞蹈形式")
                .parentId(null)
                .level(1)
                .code("TD")
                .sortOrder(3)
                .build(),
            Category.builder()
                .name("传统戏剧")
                .description("包括地方戏曲、民间戏剧、宗教戏剧等戏剧形式")
                .parentId(null)
                .level(1)
                .code("TT")
                .sortOrder(4)
                .build(),
            Category.builder()
                .name("曲艺")
                .description("包括相声、评书、快板、大鼓、弹词等说唱艺术形式")
                .parentId(null)
                .level(1)
                .code("QY")
                .sortOrder(5)
                .build(),
            Category.builder()
                .name("传统体育、游艺与杂技")
                .description("包括武术、传统体育竞技、民间游戏、杂技等")
                .parentId(null)
                .level(1)
                .code("TS")
                .sortOrder(6)
                .build(),
            Category.builder()
                .name("传统美术")
                .description("包括绘画、雕塑、建筑装饰、服饰、民间工艺美术等")
                .parentId(null)
                .level(1)
                .code("TA")
                .sortOrder(7)
                .build(),
            Category.builder()
                .name("传统技艺")
                .description("包括传统手工技艺、传统制作技艺、传统医药炮制技艺等")
                .parentId(null)
                .level(1)
                .code("TC")
                .sortOrder(8)
                .build(),
            Category.builder()
                .name("传统医药")
                .description("包括传统中医药理论、诊疗方法、制药技艺、养生保健等")
                .parentId(null)
                .level(1)
                .code("TM")
                .sortOrder(9)
                .build(),
            Category.builder()
                .name("民俗")
                .description("包括节庆习俗、人生礼俗、民间信仰、传统礼仪等民俗文化")
                .parentId(null)
                .level(1)
                .code("MS")
                .sortOrder(10)
                .build()
        );
        
        // 保存顶级分类
        List<Category> savedTopCategories = categoryRepository.saveAll(topCategories);
        
        // 获取保存后的分类ID
        Category folkLiterature = savedTopCategories.get(0); // 民间文学
        Category traditionalMusic = savedTopCategories.get(1); // 传统音乐
        Category traditionalDance = savedTopCategories.get(2); // 传统舞蹈
        Category traditionalDrama = savedTopCategories.get(3); // 传统戏剧
        Category traditionalCrafts = savedTopCategories.get(7); // 传统技艺
        Category folklore = savedTopCategories.get(9); // 民俗
        
        // 创建二级分类
        List<Category> subCategories = Arrays.asList(
            // 民间文学的子分类
            Category.builder()
                .name("神话传说")
                .description("包括创世神话、英雄传说、历史传说等")
                .parentId(folkLiterature.getId())
                .level(2)
                .code("FL01")
                .sortOrder(1)
                .build(),
            Category.builder()
                .name("民间故事")
                .description("包括生活故事、动物故事、笑话等")
                .parentId(folkLiterature.getId())
                .level(2)
                .code("FL02")
                .sortOrder(2)
                .build(),
            Category.builder()
                .name("民间歌谣")
                .description("包括劳动歌、仪式歌、生活歌等")
                .parentId(folkLiterature.getId())
                .level(2)
                .code("FL03")
                .sortOrder(3)
                .build(),
            Category.builder()
                .name("谚语俗语")
                .description("包括生产谚语、生活谚语、社会谚语等")
                .parentId(folkLiterature.getId())
                .level(2)
                .code("FL04")
                .sortOrder(4)
                .build(),
            
            // 传统音乐的子分类
            Category.builder()
                .name("民歌")
                .description("包括山歌、田歌、渔歌、牧歌等")
                .parentId(traditionalMusic.getId())
                .level(2)
                .code("TM01")
                .sortOrder(1)
                .build(),
            Category.builder()
                .name("民族器乐")
                .description("包括吹奏乐、拉弦乐、弹拨乐、打击乐等")
                .parentId(traditionalMusic.getId())
                .level(2)
                .code("TM02")
                .sortOrder(2)
                .build(),
            Category.builder()
                .name("戏曲音乐")
                .description("包括各地方戏曲的音乐部分")
                .parentId(traditionalMusic.getId())
                .level(2)
                .code("TM03")
                .sortOrder(3)
                .build(),
            Category.builder()
                .name("宗教音乐")
                .description("包括佛教音乐、道教音乐、民间信仰音乐等")
                .parentId(traditionalMusic.getId())
                .level(2)
                .code("TM04")
                .sortOrder(4)
                .build(),
            
            // 传统舞蹈的子分类
            Category.builder()
                .name("民间舞蹈")
                .description("包括汉族民间舞、少数民族舞蹈等")
                .parentId(traditionalDance.getId())
                .level(2)
                .code("TD01")
                .sortOrder(1)
                .build(),
            Category.builder()
                .name("宗教舞蹈")
                .description("包括佛教舞蹈、道教舞蹈、萨满舞等")
                .parentId(traditionalDance.getId())
                .level(2)
                .code("TD02")
                .sortOrder(2)
                .build(),
            Category.builder()
                .name("宫廷舞蹈")
                .description("包括古代宫廷雅乐舞蹈")
                .parentId(traditionalDance.getId())
                .level(2)
                .code("TD03")
                .sortOrder(3)
                .build(),
            Category.builder()
                .name("仪式舞蹈")
                .description("包括祭祀舞蹈、庆典舞蹈等")
                .parentId(traditionalDance.getId())
                .level(2)
                .code("TD04")
                .sortOrder(4)
                .build(),
            
            // 传统戏剧的子分类
            Category.builder()
                .name("京剧")
                .description("中国国粹，四大名旦等经典剧目")
                .parentId(traditionalDrama.getId())
                .level(2)
                .code("TT01")
                .sortOrder(1)
                .build(),
            Category.builder()
                .name("昆曲")
                .description("百戏之祖，世界非物质文化遗产")
                .parentId(traditionalDrama.getId())
                .level(2)
                .code("TT02")
                .sortOrder(2)
                .build(),
            Category.builder()
                .name("地方戏曲")
                .description("包括豫剧、越剧、黄梅戏、评剧等")
                .parentId(traditionalDrama.getId())
                .level(2)
                .code("TT03")
                .sortOrder(3)
                .build(),
            Category.builder()
                .name("皮影戏")
                .description("传统民间戏剧形式")
                .parentId(traditionalDrama.getId())
                .level(2)
                .code("TT04")
                .sortOrder(4)
                .build(),
            
            // 传统技艺的子分类
            Category.builder()
                .name("纺织印染技艺")
                .description("包括丝绸织造、蜡染、扎染等")
                .parentId(traditionalCrafts.getId())
                .level(2)
                .code("TC01")
                .sortOrder(1)
                .build(),
            Category.builder()
                .name("金属加工技艺")
                .description("包括铸造、锻造、金银细工等")
                .parentId(traditionalCrafts.getId())
                .level(2)
                .code("TC02")
                .sortOrder(2)
                .build(),
            Category.builder()
                .name("陶瓷烧制技艺")
                .description("包括各地名窑的制瓷技艺")
                .parentId(traditionalCrafts.getId())
                .level(2)
                .code("TC03")
                .sortOrder(3)
                .build(),
            Category.builder()
                .name("木作技艺")
                .description("包括传统建筑木作、家具制作等")
                .parentId(traditionalCrafts.getId())
                .level(2)
                .code("TC04")
                .sortOrder(4)
                .build(),
            Category.builder()
                .name("食品制作技艺")
                .description("包括传统酿造、糕点制作等")
                .parentId(traditionalCrafts.getId())
                .level(2)
                .code("TC05")
                .sortOrder(5)
                .build(),
            
            // 民俗的子分类
            Category.builder()
                .name("节庆习俗")
                .description("包括春节、清明、端午、中秋等传统节日习俗")
                .parentId(folklore.getId())
                .level(2)
                .code("MS01")
                .sortOrder(1)
                .build(),
            Category.builder()
                .name("人生礼俗")
                .description("包括诞生礼、成年礼、婚礼、丧礼等")
                .parentId(folklore.getId())
                .level(2)
                .code("MS02")
                .sortOrder(2)
                .build(),
            Category.builder()
                .name("民间信仰")
                .description("包括祖先崇拜、自然崇拜、民间神灵信仰等")
                .parentId(folklore.getId())
                .level(2)
                .code("MS03")
                .sortOrder(3)
                .build(),
            Category.builder()
                .name("生产商贸习俗")
                .description("包括农业习俗、手工业习俗、商贸习俗等")
                .parentId(folklore.getId())
                .level(2)
                .code("MS04")
                .sortOrder(4)
                .build()
        );
        
        // 保存二级分类
        categoryRepository.saveAll(subCategories);
        
        System.out.println("非遗分类数据初始化完成！");
        System.out.println("已创建 " + topCategories.size() + " 个顶级分类");
        System.out.println("已创建 " + subCategories.size() + " 个二级分类");
    }
}
