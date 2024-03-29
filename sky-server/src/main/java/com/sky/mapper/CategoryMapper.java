package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.Autofill;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {

    Page<Category> pageQuery(CategoryPageQueryDTO dto);

    @Autofill(OperationType.UPDATE)
    void update(Category category);

    @Autofill(OperationType.INSERT)
    @Insert("insert into category " +
            "(type, name, sort, status, create_time, update_time, create_user, update_user) values " +
            "(#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void add(Category category);

    @Delete("delete from category where id = #{id}")
    void deleteById(Long id);

    List<Category> listByType(Integer type);

    @Select("select status from category where id = #{id}")
    boolean isEnabled(Long id);
}
