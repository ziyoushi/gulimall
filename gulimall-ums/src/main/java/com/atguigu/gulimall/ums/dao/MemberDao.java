package com.atguigu.gulimall.ums.dao;

import com.atguigu.gulimall.ums.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author changchen
 * @email doublechang123@163.com
 * @date 2019-08-01 21:24:21
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
