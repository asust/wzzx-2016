 #include<iostream>
#include<fstream>
#include<windows.h>
using namespace std;

const int globalmount = 101;

class Base
{
public:
	Base(int id,int face,int character,int wealth,int e_face,int e_character,int e_wealth)
		:mid(id),mface(face),mcharacter(character),mwealth(wealth),expect_face(e_face),
		expect_character(e_character),expect_wealth(e_wealth){}
	~Base(){}
protected:
	int mid;
	int mface;
	int mcharacter;
	int mwealth;
	int expect_face;
	int expect_character;
	int expect_wealth;
};


class Man : public Base
{
public:
	Man(int id,int face,int chara,int wealth,int e_face,int e_chara,int e_wealth)
		:Base(id,face,chara,wealth,e_face,e_chara,e_wealth)
	{
		satisfy_f = new int[globalmount];
		fmale_id  = new int[globalmount];
	}
	~Man(){}
private:
	int *satisfy_f;
	int *fmale_id;
	static int flag;

	template<typename>
	friend class List;
	friend class Match;
};

int Man::flag = 0;

class Woman : public Base
{
public:
	Woman(int id,int face,int chara,int wealth,int e_face,int e_chara,int e_wealth,int count=0)
		:Base(id,face,chara,wealth,e_face,e_chara,e_wealth),count(0)
	{
		satisfy_m = new int[globalmount];
		memset(satisfy_m,0,globalmount*sizeof(int));
	}
	~Woman(){}
private:
	int *satisfy_m;
	int count;
	template<typename>
	friend class List;
	friend class Match;
};


template<typename T>
class List
{
public:
	List():phead(NULL),ptail(NULL){}
	~List(){}
	class Node;
	Node* get_pointer(int len)
	{
		Node* tmp = phead;
		for(int i=0;i < len;++i)
		{
			tmp = tmp->next;
		}
		return tmp;
	}
	void Insert(T value)
	{
		Node *tmp = new Node(value);
		if(phead == NULL)
		{
			phead = tmp;
			ptail = tmp;
		}
		else
		{
			ptail->next = tmp;
			ptail = tmp;
		}
	}
	void Delete(Node *p)
	{
		Node *tmp = phead;
		
		if(p == phead)
		{
			phead = phead->next;
		}
		else 
		{
			while(tmp->next != p )
			{
				tmp =tmp->next;
			}
			tmp->next = p->next;
			p->next = NULL;
		}
		delete p;
	}
	void adjust_head_tail()
	{
		phead = Node::mplisthead;
		Node *p = phead;
		while(p->next != NULL)
		{
			p = p->next;
		}
		ptail = p;
	}
private:
	class Node
	{
	public:
		Node(T val):data(val),next(NULL){}
		~Node(){}
		static void recover()
		{
			Node *pcur = mplisthead;
			pcur = mplisthead;
			for(;pcur < mplisthead+globalmount-2;++pcur)
			{
				pcur->next = pcur+1;
			}
			mpfreelist = pcur+1;
			pcur->next = NULL;
		}
		
		void* operator new(size_t size)
		{
			Node *pcur = mpfreelist;
			if(mpfreelist == NULL)
			{
				int alloc_size = globalmount*size;
				mplisthead = (Node *)new char[alloc_size];
				mpfreelist = mplisthead;
				pcur = mpfreelist;
				for(;pcur < mpfreelist+globalmount-1;++pcur)
				{
					pcur->next = pcur+1;
				}
				pcur->next = NULL;
			}
			pcur =  mpfreelist;
			mpfreelist = pcur->next;
			return pcur;
		}
		void  operator delete(void *ptr)
		{
			if(ptr == NULL)
			{
				return;
			}
			Node *tmp = (Node *)ptr;
			tmp->next = mpfreelist;
			mpfreelist = tmp;
		}
		T data;
		Node *next;
		static Node *mpfreelist;
		static Node *mplisthead;
	};
	Node *phead;
	Node *ptail;
	friend class Match;
};

template<typename T>
typename List<T>:: Node* List<T>::Node:: mpfreelist = NULL;

template<typename T>
typename List<T>:: Node* List<T>::Node:: mplisthead = NULL;



class player : public Base
{
public:
	player(int sex,int face,int chara,int wealth,int e_face,int e_chara,int e_wealth)
		:Base(-1,face,chara,wealth,e_face,e_chara,e_wealth),msex(sex){}
private:
	int msex;
	friend class Match;
	template<typename>
	friend class List;
};


class Match
{
public:
	Match()
	{
		char fmale_id[globalmount] = {0};
		char path[3][20] =  {"F:\\male.txt","F:\\female.txt","F:\\players.txt"};
		//char path[3][20] =  {"F:\\a.txt","F:\\b.txt","F:\\c.txt"};

		for(int k=0;k < 3;++k)
		{
			ifstream ifile;
			ifile.open(path[k],ios::in);
			if(!ifile)
			{
				cerr<<"open file error";
				exit(0);
			}
			int arr[7] = {0};
			char buff[12]={0};
			for(int i=0;i<globalmount-1 && !ifile.eof();++i)
			{
				for(int j=0;j < 7;++j)
				{
					if(j < 6)
					{
						ifile.getline(buff,12,',');
						arr[j] = atoi(buff);
						if(k == 1 && j == 0)
						{
							fmale_id[i] = arr[j];
						}
					}
					else
					{
						ifile.getline(buff,12,'\n');
						arr[j] = atoi(buff);
					}
				}
				switch(k)
				{
					
				case 0:
					man_list.Insert(Man(arr[0],arr[1],arr[2],arr[3],arr[4],arr[5],arr[6]));
					break;
				case 1:
					woman_list.Insert(Woman(arr[0],arr[1],arr[2],arr[3],arr[4],arr[5],arr[6]));
					break;
				case 2:
					player_list.Insert(player(arr[0],arr[1],arr[2],arr[3],arr[4],arr[5],arr[6]));
					break;
				default:break;
				}
			}
			ifile.close();
		}
	}
	void begin()
	{
		List<player>::Node *p = player_list.phead;
		List<Man>::Node *q = man_list.phead;
		List<Woman>::Node *qw = woman_list.phead;
		int fmale_id = 0;
		int male_id = 0;
		int group = 1;
		while(p != NULL)
		{
			if((p->data).msex == 0)
			{
				woman_list.Insert(Woman(-1,(p->data).mface,(p->data).mcharacter,(p->data).mwealth,(p->data).expect_face,(p->data).expect_character,(p->data).expect_wealth));
				Man::flag = 0;
			}
			else
			{
				man_list.Insert(Man(-1,(p->data).mface,(p->data).mcharacter,(p->data).mwealth,(p->data).expect_face,(p->data).expect_character,(p->data).expect_wealth));
				Man::flag = 1;
			}
			set_satisfy();
		//	sort();
			quickloop_sort();
			for(int j=0;j < globalmount-1;++j)
			{
				fmale_id = choose_fmale();
				male_id = choose_male(fmale_id);
				if(fmale_id == -1 || male_id == -1)
				{
					cout<<"第"<<group<<"组player加入:"<<male_id<<" "<<fmale_id<<endl;
					break;
				}
				eliminate(male_id,fmale_id);
			}
			if(fmale_id != -1 && male_id != -1)
			{
				cout<<"第"<<group<<"组player加入:"<<" "<<" "<<endl;
			}
			++group;
			Recoverry();
			p=p->next;
		}
	}
	void eliminate(int male_id,int fmale_id)
	{
		List<Man>::Node *p = man_list.phead;
		List<Woman>::Node *q = woman_list.phead;
		int len = globalmount-1;
		if(Man::flag == 1)
		{
			++len;
		}
		while(p != NULL)
		{
			for(int j=0;j < len-1;++j)
			{
				if((p->data).fmale_id[j] == fmale_id)
				{
					(p->data).satisfy_f[j] = -1;
					break;
				}
			}
			p=p->next;
		}
		while(q != NULL)
		{
			memset((q->data).satisfy_m,0,globalmount*sizeof(int));
			(q->data).count = 0;
			q = q->next;
		}
		p = man_list.phead;
		q = woman_list.phead;
		p = get_position_m(male_id);
		q = get_position_f(fmale_id);
		man_list.Delete(p);
		woman_list.Delete(q);
	}
	void Recoverry()
	{
		List<Man>::Node::recover();
		List<Woman>::Node::recover();
		man_list.adjust_head_tail();
		woman_list.adjust_head_tail();
		List<Man>::Node *p = man_list.phead;
		List<Woman>::Node *q = woman_list.phead;
		for(int i=0;i<globalmount-1;++i)
		{
			(q->data).count = 0;
			memset((q->data).satisfy_m,0,globalmount*sizeof(int));
			q = q->next;
		}
		for(int i=0; i<globalmount-1;++i)
		{
			memset((p->data).satisfy_f,0,globalmount*sizeof(int));
			memset((p->data).fmale_id,0,globalmount*sizeof(int));
			p = p->next;
		}
	}
	void set_player(int male_id,int fmale_id)
	{
		List<Man>::Node *p = get_position_m(male_id);
		List<Woman>::Node *q = get_position_f(fmale_id);

		(q->data).count = 0;
		memset((q->data).satisfy_m,0,globalmount*sizeof(int));
		memset((p->data).satisfy_f,0,globalmount*sizeof(int));
		memset((p->data).fmale_id,0,globalmount*sizeof(int));
	}
	void show()
	{
		List<Man>::Node *p = man_list.phead;
		
		int len = globalmount-1;
		if(Man::flag == 1)
		{
			++len;
		}
		for(int i=0;i < len;++i)
		{
			cout<<(p->data).satisfy_f[i]<<" "<<(p->data).fmale_id[i]<<endl;
		}
	}
private:
	void sort()
	{
		List<Man>::Node *p = man_list.phead;
		int len = globalmount-1;
		int flag = 0;
		if(Man::flag == 0)
		{
			++len;
		}
		while(p != NULL)
		{
			for(int i=0;i<len-1;++i)
			{
				for(int j=0;j<len-i-1;++j)
				{
					if((p->data).satisfy_f[j] < (p->data).satisfy_f[j+1])
					{
						swap((p->data).satisfy_f[j],(p->data).satisfy_f[j+1]);
						swap((p->data).fmale_id[j],(p->data).fmale_id[j+1]);
						flag = 1;
					}
				}
				if(flag == 0)
				{
					break;
				}
				flag = 0;
			}
			p=p->next;
		}
	}

	int partdivision(int *arr,int *brr,int low,int high)
	{
		int tmp = arr[low];
		int tmpex = brr[low];
		while(low < high)
		{
			while((arr[high] < tmp || arr[high] == tmp) && low < high)
			{
				--high;
			}
			arr[low] = arr[high];
			brr[low] = brr[high];
			while(arr[low] > tmp && low < high)
			{
				++low;
			}
			arr[high] = arr[low];
			brr[high] = brr[low];
		}
		arr[low] = tmp;
		brr[low] = tmpex;
		return low;
	}

	void quickloop_sort()
	{
		List<Man>::Node *p = man_list.phead;
		int len = globalmount-1;
		if(Man::flag == 0)
		{
			++len;
		}
		int *stack = new int[2*len];
		int top = 0;
		int low;
		int high;
		int part;
		stack[top++] = 0;
		stack[top++] = len-1;
		while(p != NULL)
		{
			while(top != 0)
			{
				high = stack[--top];
				low = stack[--top];
				part = partdivision((p->data).satisfy_f,(p->data).fmale_id,low,high);
				if(low < part-1)
				{
					stack[top++] = low;
					stack[top++] = part-1;
				}
				if(part+1 < high)
				{
					stack[top++] = part+1;
					stack[top++] = high;
				}
			}
			stack[top++] = 0;
			stack[top++] = len-1;
			p = p->next;
		}
		delete []stack;
	}
	void set_satisfy()
	{
		List<Man>::Node *p = man_list.phead;
		List<Woman>::Node *q = woman_list.phead;
		int len_m = globalmount-1;
		int len_f = globalmount-1;
		if(Man::flag == 1)
		{
			++len_m;
		}
		else
		{
			++len_f;
		}
		for(int i=0; i<len_m;++i)
		{
			for(int j=0;j<len_f;++j)
			{
				(p->data).fmale_id[j] = (q->data).mid;
				(p->data).satisfy_f[j] = (p->data).expect_face*(q->data).mface+(p->data).expect_character*(q->data).mcharacter+(p->data).expect_wealth*(q->data).mwealth;
				q = q->next;
			}
			q = woman_list.phead;
			p = p->next;
		}
	}
	void swap(int &a,int &b)
	{
		a^=b^=a^=b;
	}
	int choose_fmale()
	{
		List<Man>:: Node* p = man_list.phead;
		List<Woman>:: Node* q = woman_list.phead;
		int max = 0;
		int id = 0;
		int k = 0;
		int len = globalmount;
		int sum = 0;
		int i=0;
		if(Man::flag == 1)
		{
			--len;
		}
		while(p != NULL)
		{
			while((p->data).satisfy_f[i] == -1){++i;}
			max = (p->data).satisfy_f[i];
			for(;i<len && (p->data).satisfy_f[i]==max;++i)
			{
				id = (p->data).fmale_id[i];
				q = get_position_f(id);
				
				(q->data).count++;
				if((p->data).mid != 0)
				{
					(q->data).satisfy_m[k] = (p->data).mid;
				}
				else
				{
					(q->data).satisfy_m[k] = 1000; //用1000替换0号id
				}
			}
			i=0;
			++k;
			p = p->next;
		}
		max = 0;
		q = woman_list.phead;
		while(q != NULL)
		{
			if((q->data).count > max)
			{
				
				max = (q->data).count;
				id = (q->data).mid;
				sum = (q->data).mface+(q->data).mcharacter+(q->data).mwealth;
			}
			else if((q->data).count == max)
			{
				int sumex = (q->data).mface+(q->data).mcharacter+(q->data).mwealth;
				if(sumex > sum)
				{
					max = (q->data).count;
					id = (q->data).mid;
					sum = sumex;
				}
				else if(sumex == sum)
				{
					if((q->data).mid < id)
					{
						max = (q->data).count;
						id = (q->data).mid;
						sum = sumex;
					}
				}
			}
			q = q->next;
		}
		return id;
	}
	int choose_male(int fmale_id)
	{
		List<Man>:: Node* p = man_list.phead;
		List<Man>:: Node* pex = man_list.phead;
		List<Woman>:: Node* q = woman_list.phead;
		int len = globalmount-1;
		int max = -2;
		int max_id = -2;
		int sum = 0;
		int sum_id = 0;
		q = get_position_f(fmale_id);
		if(Man::flag == 1)
		{
			++len;
		}
		for(int i=0;i < len;++i)
		{
			if((sum_id = (q->data).satisfy_m[i]) != 0)
			{
				if(sum_id == 1000)
				{
					sum_id = 0;
				}
				p = get_position_m(sum_id);
				sum = (p->data).mface*(q->data).expect_face+(p->data).mcharacter*(q->data).expect_character+(p->data).mwealth*(q->data).expect_wealth;
				if(sum > max)
				{
					max = sum;
					max_id = sum_id;
				}
				else if(sum == max)
				{
					p = get_position_m(sum_id);
					pex = get_position_m(max_id);
					int sumone = (p->data).mface+(p->data).mcharacter+(p->data).mwealth;
					int sumtwo = (pex->data).mface+(pex->data).mcharacter+(pex->data).mwealth;
					if(sumone > sumtwo)
					{
						max = sum;
						max_id = sum_id;
					}
					else if(sumone == sumtwo)
					{
						if( sum_id < max_id)
						{
							max = sum;
							max_id = sum_id;
						}
					}
				}	
			}
		}
		return max_id;
	}

	List<Woman>::Node* get_position_f(int fmale_id)
	{
		List<Woman>:: Node* q = woman_list.phead;
		int len =globalmount-1;
		if(Man::flag == 0)
		{
			++len;
		}
		for(int i=0;i < len && q != NULL;++i)
		{
			if((q->data).mid == fmale_id)
			{
				return woman_list.get_pointer(i);
			}
			q = q->next;
		}
		return NULL;
	}
	List<Man>::Node* get_position_m(int male_id)
	{
		List<Man>:: Node* q = man_list.phead;
		int len =globalmount-1;
		if(Man::flag == 1)
		{
			++len;
		}
		for(int i=0;i < len && q != NULL;++i)
		{
			if((q->data).mid == male_id)
			{
				return man_list.get_pointer(i);
			}
			q = q->next;
		}
		return NULL;
	}
	List<Man> man_list;
	List<Woman> woman_list;
	List<player> player_list;
};



class MyTimer
{
public:
	MyTimer()
	{
		QueryPerformanceFrequency(&_freq);
		costTime=0.0;
	}
	void Start()
	{
		for(int i=0; i<EN_NUMER; ++i)
		{
			QueryPerformanceCounter(&_array[i]._begin);	
		}
	}
	void Stop()
	{
		for(int i=0; i<EN_NUMER; ++i)
		{
			QueryPerformanceCounter(&_array[i]._end);	
		}
	}
	void Reset()
	{
		costTime=0.0;
	}
	void showTime()
	{
		double allTime=0.0;
		for(int i=0; i<EN_NUMER; ++i)
		{
			allTime+=(((double)_array[i]._end.QuadPart-(double)_array[i]._begin.QuadPart)/(double)_freq.QuadPart);
		}
		costTime=allTime/EN_NUMER;
		costTime*=1000000;

		if((((int)costTime)/1000000) > 0)
		{
			cout<<costTime/1000000<<" s"<<endl;
		}
		else if(((int)costTime)/1000 > 0)
		{
			cout<<costTime/1000<<" ms"<<endl;
		}
		else
		{
			cout<<costTime<<" us"<<endl;
		}
	}
private:
	class Array
	{
	public:
		LARGE_INTEGER _begin;
		LARGE_INTEGER _end;
	};
	enum{EN_NUMER=5};
	LARGE_INTEGER _freq;
	double costTime;
	Array _array[EN_NUMER];
};

int main()
{
	 int x=0;
	MyTimer timer;
	timer.Start();

	Match match;
	match.begin();

	timer.Stop();
	timer.showTime();
	 cin>>x;
	return 0;
}





