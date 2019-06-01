# baseframe
数据库：
    CREATE TABLE `permission` (
      `pid` int(11) NOT NULL,
      `pname` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '权限名称',
      PRIMARY KEY (`pid`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
    
    CREATE TABLE `permission_role` (
      `rid` int(11) NOT NULL,
      `pid` int(11) NOT NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
    
    CREATE TABLE `role` (
      `rid` int(11) NOT NULL,
      `rname` varchar(255) DEFAULT NULL,
      PRIMARY KEY (`rid`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
    
    
    CREATE TABLE `user` (
      `uid` int(11) NOT NULL AUTO_INCREMENT,
      `username` varchar(255) DEFAULT NULL,
      `password` varchar(255) DEFAULT NULL,
      `salt` varchar(255) DEFAULT NULL,
      PRIMARY KEY (`uid`) USING BTREE
    ) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
    
    CREATE TABLE `user_role` (
      `uid` int(11) NOT NULL,
      `rid` int(11) NOT NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;