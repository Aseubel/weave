package com.aseubel.weave.pojo.entity.ich;

import cn.hutool.core.util.HashUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Aseubel
 * @date 2025/7/20 下午3:48
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "inheritor_category")
public class InheritorCategory {

    @EmbeddedId
    private InheritorCategoryId id;

    @ManyToOne
    @MapsId("inheritorId")
    @JoinColumn(name = "inheritor_id")
    private Inheritor inheritor;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    private Category category;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        InheritorCategory that = (InheritorCategory) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return HashUtil.identityHashCode(this);
    }

    @Embeddable
    public class InheritorCategoryId implements Serializable {

        @Column(name = "inheritor_id")
        private Long inheritorId;

        @Column(name = "category_id")
        private Long categoryId;

        @Override
        public final boolean equals(Object o) {
            if (this == o) return true;
            if (o == null) return false;
            Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
            Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
            if (thisEffectiveClass != oEffectiveClass) return false;
            InheritorCategoryId that = (InheritorCategoryId) o;
            return inheritorId != null && Objects.equals(inheritorId, that.inheritorId)
                    && categoryId != null && Objects.equals(categoryId, that.categoryId);
        }

        @Override
        public final int hashCode() {
            return Objects.hash(inheritorId, categoryId);
        }
    }
}

