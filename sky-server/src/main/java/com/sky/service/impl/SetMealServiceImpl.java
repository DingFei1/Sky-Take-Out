package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    SetMealMapper setMealMapper;

    @Autowired
    SetMealDishMapper setMealDishMapper;

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setMealMapper.page(setmealPageQueryDTO);
        long total = page.getTotal();
        List<SetmealVO> records = page.getResult();
        return new PageResult(total, records);
    }

    @Override
    public void addSetMeal(SetmealDTO setmealDTO) {
        Setmeal setMeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setMeal);
        setMealMapper.insert(setMeal);
        Long setMealId = setMeal.getId();
        for(SetmealDish setMealDish: setmealDTO.getSetmealDishes()) {
            setMealDish.setSetmealId(setMealId);
            setMealDishMapper.insert(setMealDish);
        }
    }

    @Override
    public SetmealVO searchById(Long id) {
        SetmealVO setmealVO = new SetmealVO();
        Setmeal setmeal = setMealMapper.searchById(id);
        BeanUtils.copyProperties(setmeal, setmealVO);
        List<SetmealDish> setMealDishList= setMealDishMapper.searchBySetMealId(setmeal.getId());
        setmealVO.setSetmealDishes(setMealDishList);
        return setmealVO;
    }


    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setMealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setMealMapper.getDishItemBySetmealId(id);
    }

    @Override
    public void updateSetMeal(SetmealDTO setmealDTO) {
        Setmeal setMeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setMeal);
        setMealMapper.update(setMeal);
        setMealDishMapper.deleteBySetMealId(setMeal.getId());
        List<SetmealDish> setMealDishList = setmealDTO.getSetmealDishes();

        if (setMealDishList != null && !setMealDishList.isEmpty()) {
            for (SetmealDish dish : setMealDishList) {
                dish.setDishId(setMeal.getId());
                setMealDishMapper.insert(dish);
            }
        }
    }

    @Override
    public void changeStatus(Integer status, Long id) {
        Setmeal setMeal = setMealMapper.searchById(id);
        setMeal.setStatus(status);
        setMealMapper.update(setMeal);
    }

    @Override
    public void deleteBySetMealIds(List<Long> ids) {
        setMealDishMapper.deleteBySetMealIds(ids);
        setMealMapper.deleteBySetMealIds(ids);
    }
}
