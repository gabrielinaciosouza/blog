import React from 'react';
import { Comment } from '../../models/comment';
import CommentTile from '../commentTile/CommentTile';
import styles from './commentSection.module.css';
import { getComments } from '@/services/commentService';
import Button from '../button/Button';
import Link from 'next/link';
import AddCommentModal from '../addCommentModal/AddCommentModal';

const CommentSection = async ({commentList, show, slug}: {commentList: string[], show: boolean, slug: string}) => {
    try {
        const comments: Comment[] = await getComments(commentList);

        return (
            <div className={styles.container}>
                {show && <AddCommentModal slug={slug} />}
                <div className={styles.header}>
                    <h2>Comments</h2>
                    <Link href={`/posts/${slug}/?show=true`}>
                        <Button >Add Comment</Button>
                    </Link>
                </div>
                {comments.length > 0 ? (
                    comments.map(comment => <CommentTile key={comment.commentId} {...comment} />)
                ) : (
                    <p>No comments yet.</p>
                )}
            </div>
        );
    } catch {
        return <></>;
    }


};

export default CommentSection;