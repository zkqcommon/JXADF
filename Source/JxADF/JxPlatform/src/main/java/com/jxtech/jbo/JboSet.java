package com.jxtech.jbo;

import com.jxtech.db.DBFactory;
import com.jxtech.db.DataQuery;
import com.jxtech.db.util.JxDataSourceUtil;
import com.jxtech.i18n.JxLangResourcesUtil;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxConstant;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.StrUtil;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 每个表的记录信息
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class JboSet extends BaseJboSet implements JboSetIFace {
    private static final Logger LOG = LoggerFactory.getLogger(JboSet.class);
    private static final long serialVersionUID = -847638493380867280L;
    // 工作流ID
    private String workflowId;

    /**
     * 命名规则：appname,appnameSet 子类必须覆盖此方法
     * 
     * @return
     * @throws JxException
     */
    protected JboIFace getJboInstance() throws JxException {
        currentJbo = new Jbo(this);
        return currentJbo; // 创建自己的JBO
    }

    /**
     * 查询结果集
     * 
     * @return
     */
    private List<Map<String, Object>> queryList() throws JxException {
        DataQuery dq = DBFactory.getDataQuery(this.getDbtype(), this.getDataSourceName());
        String tn = getEntityname();
        if (StrUtil.isNull(tn) || getJboname().startsWith("SQL_")) {
            tn = this.getSql();
        }
        return dq.query(tn, getQueryInfo());
    }

    /**
     * 重新进行查询结果
     * 
     * @param uid 唯一值
     */
    @Override
    public JboIFace queryJbo(String uid) throws JxException {
        if (StrUtil.isNull(getJboname()) || StrUtil.isNull(uid)) {
            return null;
        }
        currentJbo = getJboInstance();
        String uidName = getUidName();
        String where = uidName + " = ?";
        DataQuery dq = DBFactory.getDataQuery(this.getDbtype(), getDataSourceName());
        DataQueryInfo qbe = this.getQueryInfo();
        qbe.setWhereCause(where);
        qbe.setWhereParams(new Object[] { uid });
        List<Map<String, Object>> list = dq.queryAllPage(getJboname(), qbe);
        if (list != null && list.size() >= 1) {
            currentJbo.setData(list.get(0));
            currentJbo.afterLoad();
            return currentJbo;
        }
        return currentJbo;
    }

    /**
     * 重新进行查询结果
     * 
     * @param ids id序列
     */
    @Override
    public List<JboIFace> queryJbo(String[] ids) throws JxException {
        if (StrUtil.isNull(getJboname()) || ids == null) {
            return null;
        }
        currentJbo = getJboInstance();
        String uidName = getUidName();
        String where = uidName + " in (";
        for (int i = 0; i < ids.length; i++) {
            where = where + " ?,";
        }
        where = StringUtils.removeEnd(where, ",");
        where = where + " )";
        DataQuery dq = DBFactory.getDataQuery(this.getDbtype(), getDataSourceName());
        DataQueryInfo qbe = this.getQueryInfo();
        qbe.setWhereCause(where);
        qbe.setWhereParams(ids);
        List<Map<String, Object>> list = dq.queryAllPage(getJboname(), qbe);
        // List<JboIFace> data = new ArrayList<JboIFace>();
        if (list != null && list.size() > 0) {
            getJbolist().clear();
            for (Map<String, Object> map : list) {
                currentJbo = getJboInstance();
                currentJbo.setData(map);
                currentJbo.afterLoad();
                getJbolist().add(currentJbo);
            }
        }
        return getJbolist();
    }

    /**
     * 重新进行查询结果
     * 
     * @param jboKey 列
     * @param uid 唯一值
     */
    @Override
    public JboIFace queryJbo(String jboKey, String uid) throws JxException {
        if (StrUtil.isNull(getJboname()) || StrUtil.isNull(uid)) {
            return null;
        }

        if (StrUtil.isNull(jboKey)) {
            return queryJbo(uid);
        }

        currentJbo = getJboInstance();
        String where = jboKey + " = ?";
        DataQuery dq = DBFactory.getDataQuery(this.getDbtype(), this.getDataSourceName());
        DataQueryInfo qbe = this.getQueryInfo();
        qbe.setWhereCause(where);
        qbe.setWhereParams(new Object[] { uid });
        List<Map<String, Object>> list = dq.queryAllPage(getJboname(), qbe);
        if (list != null && list.size() == 1) {
            currentJbo.setData(list.get(0));
            currentJbo.afterLoad();
            return currentJbo;
        }
        return currentJbo;
    }

    /**
     * 重新进行查询结果
     * 
     * @param where 固定条件
     * @param jboKey 列
     * @param uid 唯一值
     */
    @Override
    public JboIFace queryJbo(String where, String jboKey, String uid) throws JxException {
        if (StrUtil.isNull(getJboname()) || StrUtil.isNull(uid)) {
            return null;
        }

        if (StrUtil.isNull(jboKey)) {
            return queryJbo(uid);
        }

        currentJbo = getJboInstance();
        StringBuilder sb = new StringBuilder();
        sb.append(jboKey);
        sb.append(" = ?");
        DataQuery dq = DBFactory.getDataQuery(this.getDbtype(), this.getDataSourceName());
        DataQueryInfo qbe = this.getQueryInfo();
        if (!StrUtil.isNull(where)) {
            sb.append(" and ");
            sb.append(where);
        }
        qbe.setWhereCause(sb.toString());
        qbe.setWhereParams(new Object[] { uid });
        List<Map<String, Object>> list = dq.queryAllPage(getJboname(), qbe);
        if (list != null && list.size() >= 1) {
            currentJbo.setData(list.get(0));
            currentJbo.afterLoad();
            return currentJbo;
        }
        return currentJbo;
    }

    /**
     * 查询结果集
     * 
     * @param shipname 联系名
     * @return
     * @throws JxException
     */
    @Override
    public List<JboIFace> query(String shipname) throws JxException {
        int pageNum = getQueryInfo().getPageNum();
        int pageSize = getQueryInfo().getPageSize();

        if (pageNum == 0 && pageSize == 0) {
            return queryAll();
        } else {
            if (pageNum == 0) {
                getQueryInfo().setPageNum(1);
            }

            if (pageSize == 0) {
                getQueryInfo().setPageSize(20);
            }
        }

        List<Map<String, Object>> list = queryList();
        if (list == null) {
            LOG.info("没有正确查询到结果。jboname=" + getJboname());
            return null;
        }
        getJbolist().clear();
        // 转换类型
        int size = list.size();
        for (int i = 0; i < size; i++) {
            currentJbo = getJboInstance();
            currentJbo.setData(list.get(i));
            currentJbo.afterLoad();
            getJbolist().add(currentJbo);
        }
        return getJbolist();
    }

    @Override
    public JboIFace getJboOfIndex(int index, boolean reload) throws JxException {
        if (getJbolist() == null || reload) {
            queryAll();
        }
        return super.getJboOfIndex(index);
    }

    @Override
    public boolean save(Connection conn) throws JxException {
        if (!canSave()) {
            throw new JxException(JxLangResourcesUtil.getString("jboset.cansave.false"));
        }
        if (getJbolist() == null) {
            LOG.info("没有要保存的数据，jbolist is null");
            return true;
        }
        List<JboIFace> list = getJbolist();
        int ls = list.size();
        boolean flag = true;
        for (int i = 0; i < ls; i++) {
            JboIFace jbo = list.get(i);
            if (jbo == null) {
                continue;
            }
            if (jbo.isModify() || jbo.isToBeAdd() || jbo.isToBeDel() || jbo.getChangedChildren().size() > 0) {
                flag = flag & jbo.save(conn);
            }
            if (!flag) {
                break;
            }
        }
        return flag;
    }

    /**
     * 删除多个数据，直接执行删除。
     * 
     * @param ids
     * @return
     */
    @Override
    public boolean delete(String[] ids) throws JxException {
        if (ids == null) {
            return false;
        }
        Connection conn = JxDataSourceUtil.getConnection(this.getDataSourceName());
        try {
            boolean flag = true;
            for (int i = 0; i < ids.length; i++) {
                JboIFace j = queryJbo(ids[i]);
                flag = delete(conn, j);
                if (!flag) {
                    break;
                }
            }
            if (flag) {
                LOG.debug("数据删除成功。");
                JxDataSourceUtil.commit(conn);
            } else {
                LOG.debug("数据删除失败。");
                JxDataSourceUtil.rollback(conn);
            }
            return flag;
        } finally {
            JxDataSourceUtil.close(conn);
        }
    }

    public boolean delete(Connection conn, JboIFace jbi, boolean isSave) throws JxException {
        if ((conn == null && isSave) || jbi == null) {
            throw new JxException(JxLangResourcesUtil.getString("jboset.cannot.get.conn.or.jbo.null"));
        }
        if (!jbi.delete()) {
            return false;
        }
        String[] children = jbi.getDeleteChildren();
        // 1.删除子记录
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                if (StrUtil.isNullOfIgnoreCaseBlank(children[i])) {
                    continue;
                }
                JboSetIFace js = jbi.getRelationJboSet(children[i], JxConstant.READ_RELOAD);
                if (js != null) {
                    List<JboIFace> list = js.getJbolist();
                    int count = list.size();
                    for (int j = 0; j < count; j++) {
                        JboIFace ji = list.get(j);
                        boolean b = ji.delete();
                        if (!b) {
                            return false;
                        }
                        /*
                         * if (isSave) { if (!ji.save(conn)) { return false;// 删除失败 } }
                         */
                    }
                }
            }
        }
        if (isSave) {
            // 2.删除主记录
            return jbi.save(conn);
        } else {
            return true;
        }
    }

    @Override
    public boolean commit() throws JxException {
        Connection conn = JxDataSourceUtil.getConnection(this.getDataSourceName());
        boolean flag = true;
        try {
            if (currentJbo != null) {
                flag = currentJbo.save(conn);
            }

            if (flag) {
                // currentjbo在上面已经保存过来，就要将其移除掉不需要再次保存了。
                getJbolist().remove(currentJbo);
                flag = save(conn);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            flag = false;
            throw new JxException(e.getMessage());
        } finally {
            // 当全部保存完成后，在将currentJbo放到jbolist中
            getJbolist().add(currentJbo);
            setCurrentJbo(currentJbo);

            if (flag) {
                LOG.debug("数据保存成功。");
                JxDataSourceUtil.commit(conn);
                setFlag();
            } else {
                LOG.info("数据保存失败。");
                JxDataSourceUtil.rollback(conn);
            }
            JxDataSourceUtil.close(conn);
        }
        return flag;
    }

    @Override
    public boolean rollback() throws JxException {
        boolean flag = true;
        List<JboIFace> removeJboList = new ArrayList<JboIFace>();
        if (getJbolist().isEmpty() && null != currentJbo) {
            getJbolist().add(currentJbo);
        }
        try {
            for (JboIFace jbo : getJbolist()) {
                if (null != jbo) {
                    JboIFace removeJbo = jbo.rollback();
                    if (null != removeJbo) {
                        removeJboList.add(removeJbo);
                    }
                }
            }
            getJbolist().removeAll(removeJboList);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            flag = false;
            throw new JxException(e.getMessage());
        }

        return flag;
    }

    @Override
    public String getWorkflowId() {
        if (workflowId != null) {
            return workflowId;
        }
        String appName = getAppname();
        if (!StrUtil.isNull(appName)) {
            try {
                JboIFace maxappswfinfoJbo = JboUtil.getJbo("MAXAPPSWFINFO", "APP", appName.toUpperCase());
                if (null != maxappswfinfoJbo) {
                    workflowId = maxappswfinfoJbo.getString("PROCESS");
                }
            } catch (JxException jxex) {
                jxex.printStackTrace();
            }
        }
        return workflowId;
    }

    @Override
    public void setWorkflowId(String wfId) {
        workflowId = wfId;
    }

    @Override
    public int count() throws JxException {
        DataQuery dq = DBFactory.getDataQuery(this.getDbtype(), this.getDataSourceName());
        int count = dq.count(this);
        setCount(count);
        return count;
    }

    @Override
    public void getBlob(String blobColumnName, String uid, OutputStream os) throws JxException {
        if (StrUtil.isNull(uid) || os == null) {
            throw new JxException(JxLangResourcesUtil.getString("jboset.getblob.get.id.or.os.null"));
        }
        DataQuery dq = DBFactory.getDataQuery(this.getDbtype(), this.getDataSourceName());
        dq.getBlob(getJboname(), blobColumnName, getUidName(), uid, os);
    }

    /**
     * 发送工作流
     * 
     * @return
     * @throws JxException
     */
    @Override
    public boolean route() throws JxException {
        return false;
    }

    /**
     * 记录集加载完后执行的方法
     */
    @Override
    public void afterLoad() throws JxException {
        JboIFace parent = getParent();
        if (null != parent) {
            String parentStatus = parent.getString("wft_status");
            if (!StrUtil.isNull(parentStatus) && "CLOSE".equalsIgnoreCase(parentStatus)) {
                setReadonly(true);
            }
        }
    }

    @Override
    public void addMpp(List<Task> tasks, Map<String, String> paramMap, Map<String, String> initMap) throws JxException {
        int jumpIndex = 0;
        for (Task task : tasks) {
            String wbs = task.getWBS();
            if (null != wbs && !"0".equals(wbs)) {
                JboIFace jbo = add();
                jbo.addMpp(task, paramMap, initMap);
                int temp = task.getID() - jumpIndex;
                task.setID(temp);
            } else {
                jumpIndex++;
            }
        }
        commit();
    }

    @Override
    public void expMpp(ProjectFile project, Map<String, String> paramMap, Map<String, String> initMap) throws JxException {

    }

    @Override
    public String loadImportFile(List<Map<Object, String>> importFileResult, JxUserInfo userInfo) throws JxException {
        return "导入成功!";
    }

    @Override
    public void lookup(List<JboIFace> lookupList) throws JxException {
        if (!lookupList.isEmpty()) {
            for (JboIFace lookupJbo : lookupList) {
                JboIFace tempJbo = add();
                tempJbo.getData().putAll(lookupJbo.getData());
            }
        }
    }

}
