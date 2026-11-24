package online.futuret.taskflow.data.repository

import online.futuret.taskflow.data.local.dao.BoardDao
import online.futuret.taskflow.data.local.entity.BoardEntity

class BoardRepository(
    private val boardDao: BoardDao
) {
    fun getBoardsByProject(projectId: Int) = boardDao.getByProject(projectId)
    suspend fun insertBoard(board: BoardEntity) = boardDao.insert(board)
    suspend fun updateBoard(board: BoardEntity) = boardDao.update(board)
    suspend fun deleteBoard(board: BoardEntity) = boardDao.delete(board)
    suspend fun getBoardById(boardId: Int) = boardDao.getById(boardId)
}