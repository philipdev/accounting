import { BehaviorSubject, Subject, Observable, catchError, throwError, map, tap, switchMap, retry, filter } from 'rxjs';
import { HttpClient, HttpErrorResponse, HttpResponse } from "@angular/common/http";


export class HttpCrudServiceBase<Type extends IDType> {
	
	protected readonly list$ = new BehaviorSubject<Type[]>([]);
    public readonly status$ = new BehaviorSubject<FetchStatus>(FetchStatus.INITIAL);
    protected readonly fetch$ = new Subject<string>();
  
  
	constructor(protected readonly uri:string, protected http:HttpClient) {
		this.fetch$.pipe(
			switchMap(uri=> this.http.get<Type[]>(uri)),
			retry()
		).subscribe(a=>this.fetchedOk(a), e=>this.fetchedError(e));
	}
	
	
	public fetch() {
		this.fetch$.next(this.uri);
	}

	private fetchedOk(a:Type[]) {
		this.list$.next(a); 
		this.status$.next(FetchStatus.OK);
	}

	private fetchedError(error: HttpErrorResponse) {
		this.status$.next(FetchStatus.FAILED);
	}
	
	protected handleError(error: HttpErrorResponse) {
		let code = "GeneralError";
		if (error.status === 404) {
			code = "NotFound";
		} 
		console.log(error);
		return throwError(() => code);
	}
	
	public getList(): Observable<Type[]> {
		return this.list$.asObservable();
	}
	
	public get(id:number): Observable<Type> {
		return this.getList().pipe(
			map(
				comp => comp.find(c=> c.id == id)
			),
			filter(comp=> comp !== undefined),
		) as Observable<Type>;
	}
  
	public create(data:Type):Observable<Type> {
		return this.http.post<Type>( this.uri, data).pipe(
			catchError(this.handleError),
			tap(() => this.fetch())
		);
	} 
	
	public update(id:number, data:Type): Observable<Type> {
		return this.http.post<Type>( this.uri + '/' + id, data).pipe(
			catchError(this.handleError),
			tap(() => this.fetch())
		);
	}
	
	public deleteByIds(ids:number[]) {

		return this.http.delete<void>(this.uri, {body: ids}).pipe (
			catchError(this.handleError),
			tap(() => this.fetch())
		);
	}
}
export enum FetchStatus {
	INITIAL,
	OK,
	PENDING,
	FAILED
}

interface IDType {
	id?:number;
}
