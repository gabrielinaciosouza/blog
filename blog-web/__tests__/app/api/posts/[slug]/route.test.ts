import { DELETE } from '@/app/api/posts/[slug]/route';
import { deletePost } from '@/services/postService';
import { NextRequest, NextResponse } from 'next/server';

jest.mock('@/services/postService');

jest.mock('next/server', () => ({
    ...jest.requireActual('next/server'),
    NextRequest: jest.fn().mockImplementation((url: string) => ({
        url,
        nextUrl: {
            pathname: new URL(url).pathname,
            search: new URL(url).search,
            clone: jest.fn().mockReturnValue({
                pathname: new URL(url).pathname,
                search: new URL(url).search,
            }),
        },
        headers: {
            get: jest.fn().mockImplementation(key => key === 'host' ? 'localhost' : undefined),
        },
        json: jest.fn().mockResolvedValue({
            posts: [],
            count: 0,
        }),
    })),
}));

describe('DELETE /api/posts/[slug]', () => {
    beforeEach(() => {
        jest.spyOn(NextResponse, 'json').mockImplementation((body) => {
            return {
                json: jest.fn().mockResolvedValue(body)
            } as any;
        });
    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    it('should delete the post and return a success message', async () => {
        const slug = 'test-post';
        const req = new NextRequest('http://localhost/api/posts/test-post');
        const params = { slug };

        (deletePost as jest.Mock).mockResolvedValueOnce(undefined);

        const response = await DELETE(req, { params: Promise.resolve(params) });

        expect(deletePost).toHaveBeenCalledWith(slug);
        const jsonResponse = await response.json();
        expect(jsonResponse).toEqual({ message: 'Post deleted successfully' });
    });

    it('should return an error message if something goes wrong', async () => {
        const slug = 'test-post';
        const req = new NextRequest('http://localhost/api/posts/test-post');
        const params = { slug };

        (deletePost as jest.Mock).mockRejectedValueOnce(new Error('Failed to delete post'));

        const response = await DELETE(req, { params: Promise.resolve(params) });

        expect(deletePost).toHaveBeenCalledWith(slug);
        const jsonResponse = await response.json();
        expect(jsonResponse).toEqual({ message: 'Something went wrong!' });
    });
});