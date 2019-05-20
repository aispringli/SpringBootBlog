package com.f5tv.springbootblog.service.blog;

import com.f5tv.springbootblog.entity.blog.FollowEntity;
import com.f5tv.springbootblog.entity.core.ResponseResult;
import com.f5tv.springbootblog.entity.user.UserEntity;
import com.f5tv.springbootblog.mapper.blog.FollowMapper;
import com.f5tv.springbootblog.mapper.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author SpringLee
 * @Title: FollowService
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 11:38 2019/4/25
 */
@Service
public class FollowService {

    @Autowired
    FollowMapper followMapper;

    @Autowired
    UserMapper userMapper;


    public ResponseResult followInsert(FollowEntity followEntity) {
        if (userMapper.userEntitySelectByUserId(followEntity.getUserId()) == null) return new ResponseResult(1, "用户不存在，关注失败");
        if (followMapper.selectUserIsFollow(followEntity) != null) return new ResponseResult(2, "您已关注过了，无需重复关注");
        if (followMapper.insert(followEntity) > 0) {
            UserEntity userEntity=new UserEntity();
            userEntity.setUserId(followEntity.getUserId());
            userEntity.setUserFollowerQuantity(1);
            userMapper.updateuserFollowerQuantityc(userEntity);
            return new ResponseResult(0, true, "关注成功");
        }
        else return new ResponseResult(3, "关注失败");
    }

    public boolean CheckUserIsFollowByUserId(FollowEntity followEntity) {
        return followMapper.selectUserIsFollow(followEntity)!=null;
    }

    public ResponseResult followDelete(FollowEntity followEntity) {
        if (followMapper.deleteByFollowId(followEntity) > 0) {
            UserEntity userEntity=new UserEntity();
            userEntity.setUserId(followEntity.getUserId());
            userEntity.setUserFollowerQuantity(-1);
            userMapper.updateuserFollowerQuantityc(userEntity);
            return new ResponseResult(0, true, "取消关注成功");
        }
        else return new ResponseResult(3, "取消关注失败");
    }

    public List<FollowEntity> selectUserFollowByUserFollowId(long userFollowId) {
        return followMapper.selectUserFollowByUserFollowId(userFollowId);
    }

    public int selectCountUserFollowByUserFollowId(long userFollowId) {
        return followMapper.selectCountUserFollowByUserFollowId(userFollowId);
    }

    public List<FollowEntity> selectFollowersByUserId(long userId) {
        return followMapper.selectFollowersByUserId(userId);
    }

    public int selectCountFollowersByUserId(long userId){
        return followMapper.selectCountFollowersByUserId(userId);
    }
}
