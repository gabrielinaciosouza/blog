import { GET } from '@/app/api/posts/deleted/route';
import { getDeletedPosts } from '@/services/postService';
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

describe('GET /api/posts/deleted', () => {

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

    it('should return the list of deleted posts', async () => {
        const req = new NextRequest('http://localhost/api/posts/deleted');
        const posts = [
            { postId: '1', title: 'Deleted Post 1', creationDate: '2025-02-25', slug: 'deleted-post-1' },
            { postId: '2', title: 'Deleted Post 2', creationDate: '2025-02-26', slug: 'deleted-post-2' },
        ];

        (getDeletedPosts as jest.Mock).mockResolvedValueOnce(posts);

        const response = await GET(req);

        expect(getDeletedPosts).toHaveBeenCalled();
        const jsonResponse = await response.json();
        expect(jsonResponse).toEqual(posts);
    });

    it('should return an error message if something goes wrong', async () => {
        const req = new NextRequest('http://localhost/api/posts/deleted');

        (getDeletedPosts as jest.Mock).mockRejectedValueOnce(new Error('Failed to fetch deleted posts'));

        const response = await GET(req);

        expect(getDeletedPosts).toHaveBeenCalled();
        const jsonResponse = await response.json();
        expect(jsonResponse).toEqual({ message: 'Something went wrong!' });
    });
});